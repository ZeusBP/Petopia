package com.example.api_petopia.repository;

import com.example.api_petopia.entity.CartItem;
import com.example.api_petopia.entity.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findAllByShoppingCartId(Long shoppingCartId);

    void deleteAllByShoppingCartId(Long shoppingCartId );


}
