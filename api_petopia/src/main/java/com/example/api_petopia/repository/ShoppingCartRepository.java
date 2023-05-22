package com.example.api_petopia.repository;

import com.example.api_petopia.entity.ShoppingCart;
import com.example.api_petopia.entity.myenum.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUserId(Long userId);

}
