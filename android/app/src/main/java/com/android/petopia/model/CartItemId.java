package com.android.petopia.model;

import java.io.Serializable;

public class CartItemId implements Serializable {
    private Long shoppingCartId;
    private Long productId;

    public CartItemId() {
    }

    public CartItemId(Long shoppingCartId, Long productId) {
        this.shoppingCartId = shoppingCartId;
        this.productId = productId;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
