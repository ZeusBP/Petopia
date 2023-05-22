package com.example.api_petopia.repository;

import com.example.api_petopia.entity.Order;
import com.example.api_petopia.entity.event.OrderEvent;
import com.example.api_petopia.entity.myenum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findAllBy(Pageable pageable);
    Order findByOrderDetails(OrderEvent orderEvent);

    @Query("SELECT max(o.id) FROM Order o")
    Long findMaxId();

    List<Order> findAllByStatus(OrderStatus status);

}

