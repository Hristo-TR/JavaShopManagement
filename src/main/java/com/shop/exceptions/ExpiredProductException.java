package com.shop.exceptions;

import com.shop.model.product.Product;

public class ExpiredProductException extends Exception {
    private static final long serialVersionUID = 1L;

    private Product product;

    public ExpiredProductException(String message) {
        super(message);
    }

    public ExpiredProductException(Product product) {
        super("Cannot sell expired product: " + product.getName());
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
} 