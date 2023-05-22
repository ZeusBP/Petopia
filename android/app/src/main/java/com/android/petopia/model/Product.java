package com.android.petopia.model;

import java.io.Serializable;
import java.math.BigDecimal;


public class Product implements Serializable {
    private long id;
    private String name;
    private int qty;
    private Category category;
    private BigDecimal price;
    private String thumbnail;
    private String image;
    private String description;

    public Product(long id, String name, int qty, Category category, BigDecimal price, String thumbnail, String image, String description) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.category = category;
        this.price = price;
        this.thumbnail = thumbnail;
        this.image = image;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product() {
    }


}
