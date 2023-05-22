package com.example.api_petopia.service;

import com.example.api_petopia.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderService {
    Order placeOrder(Long orderId);
}
