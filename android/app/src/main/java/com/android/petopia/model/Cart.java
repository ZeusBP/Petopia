package com.android.petopia.model;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private long id;
    private User user;
    private List<CartItem> items;

    public Cart() {

    }

    public Cart(long id, User user, List<CartItem> items) {
        this.id = id;
        this.user = user;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
