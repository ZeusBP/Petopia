package com.example.api_petopia.service;


import com.example.api_petopia.entity.Order;
import com.example.api_petopia.entity.OrderDetail;
import com.example.api_petopia.repository.OrderDetailRepository;
import com.example.api_petopia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;

//    public Optional<OrderDetail> findById() {
//        return orderDetailRepository.findById(id);
//    }
    public Page<OrderDetail> searchAllForAdmin(Specification<OrderDetail> specification, int page, int limit){
        return orderDetailRepository.findAll(specification, PageRequest.of(page, limit, Sort.by("updatedAt").descending()));
    }

    public List<OrderDetail> findByOrder(Long id ){
        return  orderDetailRepository.findAllByOrderId(id);
    }


}
