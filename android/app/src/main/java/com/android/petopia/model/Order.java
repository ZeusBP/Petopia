package com.android.petopia.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Serializable {
    private Long id;
    private User user;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private boolean isShoppingCart;
    private String address;
    private String name;
    private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(Long id, User user, BigDecimal totalPrice, OrderStatus status, boolean isShoppingCart, String address, String name, List<OrderDetail> orderDetails) {
        this.id = id;
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.isShoppingCart = isShoppingCart;
        this.address = address;
        this.name = name;
        this.orderDetails = orderDetails;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isShoppingCart() {
        return isShoppingCart;
    }

    public void setShoppingCart(boolean shoppingCart) {
        isShoppingCart = shoppingCart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

}
