package com.shop.exceptions;

import com.shop.model.product.Product;

public class InsufficientQuantityException extends Exception {
    private static final long serialVersionUID = 1L;

    private Product product;
    private int requestedQuantity;
    private int availableQuantity;

    public InsufficientQuantityException(String message) {
        super(message);
    }

    public InsufficientQuantityException(Product product, int requestedQuantity, int availableQuantity) {
        super("Insufficient quantity for product: " + product.getName() +
                ". Requested: " + requestedQuantity + ", Available: " + availableQuantity);
        this.product = product;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public int getShortage() {
        return requestedQuantity - availableQuantity;
    }
} 