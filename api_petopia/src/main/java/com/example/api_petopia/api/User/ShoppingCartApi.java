package com.example.api_petopia.api.User;

import com.example.api_petopia.entity.CartItem;
import com.example.api_petopia.entity.ShoppingCart;
import com.example.api_petopia.entity.User;
import com.example.api_petopia.service.ShoppingCartService;
import com.example.api_petopia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users/cart")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ShoppingCartApi {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, path = "/add")
    public ResponseEntity<?> addShoppingCart(Principal principal, @Param("productId") long productId, @Param("quantity") int quantity){
        Optional<User> optionalUser = userService.findByNameActive(principal.getName());
        if (!optionalUser.isPresent()){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        ShoppingCart shoppingCart = shoppingCartService.addToShoppingCart(optionalUser.get(), productId, quantity);
        if (shoppingCart == null){
            return ResponseEntity.badRequest().body("Add fail");
        }
        return ResponseEntity.ok(shoppingCart) ;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findAll(Principal principal){
        Optional<User> optionalUser = userService.findByNameActive(principal.getName());
        if (!optionalUser.isPresent()){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        User user = optionalUser.get();
        return ResponseEntity.ok(shoppingCartService.getAllCart(user.getId()));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/delete")
    public ResponseEntity<?> deleteCartItem(@Param("shoppingCatId") long shoppingCatId, @Param("productId") long productId, Principal principal){
        Optional<User> optionalUser = userService.findByNameActive(principal.getName());
        if (!optionalUser.isPresent()){
            return ResponseEntity.badRequest().body("User does not exist");
        }
        ShoppingCart shoppingCart = shoppingCartService.deleteCartItem(shoppingCatId, productId);
        if (shoppingCart==null){
            return ResponseEntity.badRequest().body("delete fail");
        }
        return ResponseEntity.ok(shoppingCart) ;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/update")
    public ResponseEntity<?> updateCart(@Param("shoppingCatId") long shoppingCatId, @Param("productId") long productId, @Param("quantity") int quantity){
        ShoppingCart shoppingCart = shoppingCartService.updateQuantity(shoppingCatId, productId, quantity);
        if (shoppingCart==null){
            return ResponseEntity.badRequest().body("update fail");
        }
        return ResponseEntity.ok(shoppingCart);
    }
}
