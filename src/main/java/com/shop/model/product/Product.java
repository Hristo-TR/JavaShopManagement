package com.shop.model.product;

import com.shop.enums.ProductCategory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Product implements Serializable {
    private int id;
    private String name;
    private double purchasePrice;
    private LocalDate expirationDate;
    private int quantity;
    private ProductCategory category;

    public Product() {
    }

    public Product(int id, String name, double purchasePrice, LocalDate expirationDate, int quantity, ProductCategory category) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
        this.category = category;
    }

    public abstract double calculateSellingPrice(int daysBeforeDiscountApplies, double discountPercentage, double markupPercentage);

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public int daysUntilExpiration() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasePrice=" + purchasePrice +
                ", expirationDate=" + expirationDate +
                ", quantity=" + quantity +
                ", category=" + category +
                '}';
    }
} 