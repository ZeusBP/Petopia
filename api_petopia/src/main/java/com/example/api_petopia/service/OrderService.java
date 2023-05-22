package com.example.api_petopia.service;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.OrderStatus;

import com.example.api_petopia.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService implements IOrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    ShoppingCartService shoppingCartService;

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
    public Order saveCart(Order order) {
        return orderRepository.save(order);
    }

    public Map<String, Object> findAll(Pageable pageable) {
        Map<String, Object> responses = new HashMap<>();
        Page<Order> pageTotal = orderRepository.findAllBy(pageable);
        List<Order> list = pageTotal.getContent();
        responses.put("content", list);
        responses.put("currentPage", pageTotal.getNumber() + 1);
        responses.put("totalItems", pageTotal.getTotalElements());
        responses.put("totalPage", pageTotal.getTotalPages());
        return responses;
    }

    @Override
    @Transactional
    public Order placeOrder(Long id) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findById(id);
        if (shoppingCartOptional.isPresent()) {
            ShoppingCart shoppingCart = shoppingCartOptional.get();
            Set<OrderDetail> orderDetailSet = new HashSet<>();
            OrderDetail dto = null;
            for (CartItem cartItem:
                    shoppingCart.getItems()) {
                dto = new OrderDetail();
                dto.setId(new OrderDetailId(orderRepository.findMaxId() + 1, cartItem.getProduct().getId()));
                dto.setOrder(Order.builder().id(orderRepository.findMaxId() + 1).build());
                dto.setProduct(cartItem.getProduct());
                dto.setQuantity(cartItem.getQuantity());
                dto.setUnitPrice(cartItem.getProduct().getPrice());
                dto.setQuantity(cartItem.getQuantity());
                dto.setUpdatedAt(LocalDateTime.now());
                orderDetailSet.add(dto);

            }
//            for (int i = 0; i <orderDetailSet.size(); i++) {
//
//            }
            Order order = Order.builder()
                    .status(OrderStatus.PROCESSING)
                    .user(shoppingCart.getUser())
                    .orderDetails(orderDetailSet)
                    .totalPrice(shoppingCart.getItems().stream().map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add))
                    .name(shoppingCart.getUser().getFullName())
                    .address(shoppingCart.getUser().getAddress())
                    .isShoppingCart(false)
                    .build();
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailSet);
            if (!order.isShoppingCart()) {
                CartItemId cartItemId = new CartItemId(id,dto.getProduct().getId());
                Optional<CartItem> cartItem1 = cartItemRepository.findById(cartItemId);
                for (int i = 0; i <cartItem1.get().getShoppingCart().getItems().size(); i++) {
                    shoppingCartService.deleteCartItem(id,dto.getProduct().getId());
                }

//                shoppingCartService.deleteAllCart(id);
            }
//            Optional<Product> product = productRepository.findById(dto.getProduct().getId());
//            product.get().setQty((product.get().getQty()-dto.getQuantity()));
//            product.get().setSold(product.get().getSold()+dto.getQuantity());
            return order;
        }
        return null;
    }
    public Page<Order> searchAllForAdmin(Specification<Order> specification, int page, int limit){
        return orderRepository.findAll(specification, PageRequest.of(page, limit, Sort.by("updatedAt").descending()));
    }

    public int totalOrder(){
        return orderRepository.findAll().size();
    }
    public int totalOrderByStatus(int status){
        OrderStatus status1 = OrderStatus.CONFIRMED;
        if (status == 0){
            status1 = OrderStatus.PENDING;
        }
        if (status == 2){
            status1 = OrderStatus.CANCELLED;
        }
        if (status == 3){
            status1 = OrderStatus.DONE;
        }
        if (status == 4){
            status1 = OrderStatus.PROCESSING;
        }
        return orderRepository.findAllByStatus(status1).size();
    }


}