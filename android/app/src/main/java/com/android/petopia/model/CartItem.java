package com.android.petopia.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private CartItemId id;
    private int quantity;
    private Cart shoppingCart;
    private Product product;

    public CartItem() {
    }

    public CartItem(CartItemId id, int quantity, Cart shoppingCart, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.shoppingCart = shoppingCart;
        this.product = product;
    }

    public CartItemId getId() {
        return id;
    }

    public void setId(CartItemId id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Cart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(Cart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
