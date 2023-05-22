package com.example.api_petopia.api.User;

import com.example.api_petopia.entity.Order;
import com.example.api_petopia.entity.OrderDetail;
import com.example.api_petopia.entity.ShoppingCart;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.entity.myenum.OrderStatus;
import com.example.api_petopia.repository.ShoppingCartRepository;
import com.example.api_petopia.service.*;
import com.example.api_petopia.spec.User.Specifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "api/v1/users/orders")
public class OrderApi {
    private final IOrderService iOrderService;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private OrderDetailService orderDetailService;

    public OrderApi(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }


    @RequestMapping(method = RequestMethod.POST, path = "/checkout")
    public ResponseEntity<?> placeOrder(@RequestParam long shoppingCartId,
                                        Principal principal) {

        User user = profileService.findByUsername(principal.getName());
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (optionalShoppingCart.isPresent()){
            if (shoppingCartId > 0) {
                Order result = iOrderService.placeOrder(shoppingCartId);
                if (result != null) {
                    return ResponseEntity.ok(result);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping( method = RequestMethod.POST,path = "/getmyorder")
    public Page<Order> searchOrder(@RequestParam(value = "name", required = false) String name,
                                   @RequestParam(value = "address", required = false) String address,
                                   @RequestParam(value = "status", required = false) OrderStatus status,
                                   @RequestParam(value = "user", required = false) User user,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Specification<Order> specification = Specifications.OrderSpec(name,address,status,user,page,limit);
        return orderService.searchAllForAdmin(specification,page,limit);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByOrderId(@PathVariable Long id) {
        List<OrderDetail> order = orderDetailService.findByOrder(id);
        return ResponseEntity.ok(order);
    }
}
