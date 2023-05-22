package com.example.api_petopia.repository;

import com.example.api_petopia.entity.CartItem;
import com.example.api_petopia.entity.Order;
import com.example.api_petopia.entity.OrderDetail;
import com.example.api_petopia.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> , JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findAllByOrderId(Long id);


}

