package com.example.api_petopia.service;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.ProductSimpleStatus;
import com.example.api_petopia.repository.CartItemRepository;
import com.example.api_petopia.repository.ProductRepository;
import com.example.api_petopia.repository.ShoppingCartRepository;
import com.example.api_petopia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;

    public ShoppingCart addToShoppingCart(User user, Long productId, int quantity){
        Optional<Product> optionalProduct = productRepository.findByIdAndStatus(productId, ProductSimpleStatus.ACTIVE);
        if (!optionalProduct.isPresent()){
            return null;
        }
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (optionalShoppingCart.isPresent()){
            Set<CartItem> cartItems = optionalShoppingCart.get().getItems();
            boolean exits = false;
            for (CartItem item:cartItems
            ) {
                if (item.getProduct().getId().equals(productId)){
                    exits = true;
                    item.setQuantity(item.getQuantity() + quantity);
                    if (item.getQuantity() > optionalProduct.get().getQty()){
                        item.setQuantity(optionalProduct.get().getQty());
                    }
                }
            }
            if (!exits){
                CartItem item = new CartItem();
                item.setProduct(optionalProduct.get());
                item.setQuantity(quantity);
                item.setShoppingCart(optionalShoppingCart.get());
                CartItemId cartItemId = new CartItemId(optionalShoppingCart.get().getId(), productId);
                item.setId(cartItemId);
                cartItems.add(item);
            }
            optionalShoppingCart.get().setItems(cartItems);
            return shoppingCartRepository.save(optionalShoppingCart.get());
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            Set<CartItem> cartItems = new HashSet<>();
            CartItem cartItem = new CartItem();
            cartItem.setProduct(optionalProduct.get());
            if (quantity > optionalProduct.get().getQty()){
                quantity = optionalProduct.get().getQty();
            }
            cartItem.setQuantity(quantity);
            cartItems.add(cartItem);
            CartItemId cartItemId = new CartItemId(shoppingCart.getId(), productId);
            cartItem.setId(cartItemId);
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.setItems(cartItems);
            shoppingCart.setUser(user);
            return shoppingCartRepository.save(shoppingCart);
        }
    }

    public List<CartItem> getAllCart(Long userId){
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (!shoppingCart.isPresent()){
            return null;
        }
        Long shoppingCartId = shoppingCart.get().getId();
        List<CartItem> cartItemList = cartItemRepository.findAllByShoppingCartId(shoppingCartId);
        return cartItemList;
    }
    public ShoppingCart deleteCartItem(Long shoppingCartId, Long productId){
        Optional<Product> optionalProduct = productRepository.findByIdAndStatus(productId, ProductSimpleStatus.ACTIVE);
        if (!optionalProduct.isPresent()){
            return null;
        }
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return null;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        boolean exits = false;
        for (CartItem item:cartItems
        ) {
            if (item.getProduct().getId().equals(productId)){
                exits = true;
                cartItems.remove(item);
                CartItemId cartItemId = new CartItemId(shoppingCartId, productId);
                cartItemRepository.deleteById(cartItemId);
            }
        }
        if (!exits){
            return null;
        }
        shoppingCart.setItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    public ShoppingCart updateQuantity(Long shoppingCartId, Long productId, int quantity){
        Optional<Product> optionalProduct = productRepository.findByIdAndStatus(productId, ProductSimpleStatus.ACTIVE);
        if (!optionalProduct.isPresent()){
            return null;
        }
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return null;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        for (CartItem item:cartItems
        ) {
            if (item.getProduct().getId().equals(productId)){
                item.setQuantity(quantity);
                shoppingCart.setItems(cartItems);
                cartItemRepository.save(item);
            }
        }
        return shoppingCartRepository.save(shoppingCart);
    }

    public void deleteAllCart(Long shoppingCartId){
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (!optionalShoppingCart.isPresent()){
            return;
        }
        ShoppingCart shoppingCart = optionalShoppingCart.get();
        Set<CartItem> cartItems = shoppingCart.getItems();
        for (CartItem item:cartItems
        ) {
            cartItemRepository.deleteAllByShoppingCartId(item.getShoppingCart().getId());
        }
    }

}