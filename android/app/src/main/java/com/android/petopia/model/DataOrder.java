package com.android.petopia.model;

import java.util.List;

public class DataOrder {
    private List<Order> content;

    public DataOrder() {
    }

    public DataOrder(List<Order> content) {
        this.content = content;
    }

    public List<Order> getContent() {
        return content;
    }

    public void setContent(List<Order> content) {
        this.content = content;
    }
}
