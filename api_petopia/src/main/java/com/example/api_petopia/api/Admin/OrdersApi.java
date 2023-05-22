package com.example.api_petopia.api.Admin;

import com.example.api_petopia.entity.*;
import com.example.api_petopia.entity.myenum.OrderStatus;
import com.example.api_petopia.service.OrderDetailService;
import com.example.api_petopia.service.OrderService;
import com.example.api_petopia.spec.Admin.Specifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/admin/orders")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrdersApi {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @RequestMapping( method = RequestMethod.GET)
    public Page<Order> searchOrder(@RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "address", required = false) String address,
                                     @RequestParam(value = "status", required = false) OrderStatus status,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        Specification<Order> specification = Specifications.OrderSpec(name,address,status,page,limit);
        return orderService.searchAllForAdmin(specification,page,limit);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/change/{id}")
    public ResponseEntity<Order> change(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderService.findById(id);
        if (!optionalOrder.isPresent()) {
            ResponseEntity.badRequest().build();
        }
        Order existOrder = optionalOrder.get();
        existOrder.setStatus(OrderStatus.CONFIRMED);
        existOrder.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(orderService.save(existOrder));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findByOrderId(@PathVariable Long id) {
        List<OrderDetail> order = orderDetailService.findByOrder(id);
        return ResponseEntity.ok(order);
    }
    @RequestMapping(method = RequestMethod.GET, path = "/total")
    public int totalOrder(){
        return orderService.totalOrder();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/totalByStatus/{status}")
    public int totalOrderByStatus(@PathVariable int status){
        return orderService.totalOrderByStatus(status);
    }
}
