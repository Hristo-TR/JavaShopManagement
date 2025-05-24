package com.shop.model.store;

import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.product.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, Product> products;

    public Inventory() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void updateQuantity(int productId, int quantityChange) throws InsufficientQuantityException {
        Product product = products.get(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with ID " + productId + " not found");
        }

        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new InsufficientQuantityException(product, Math.abs(quantityChange), product.getQuantity());
        }

        product.setQuantity(newQuantity);
    }

    public Product getProductById(int productId) {
        return products.get(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public List<Product> getExpiredProducts() {
        return products.values().stream()
                .filter(Product::isExpired)
                .collect(Collectors.toList());
    }

    public Map<Integer, Product> getProducts() {
        return products;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }
} 