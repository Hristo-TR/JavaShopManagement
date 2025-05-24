package com.shop.model.sales;

import com.shop.enums.PaymentMethod;
import com.shop.model.person.Cashier;
import com.shop.model.product.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private int saleId;
    private LocalDateTime saleDateTime;
    private Cashier cashier;
    private Map<Product, Integer> items;
    private PaymentMethod paymentMethod;
    private double totalAmount;
    private boolean isCompleted;
    private Receipt receipt;

    public Sale() {
        this.items = new HashMap<>();
        this.saleDateTime = LocalDateTime.now();
        this.isCompleted = false;
    }

    public Sale(int saleId, Cashier cashier) {
        this();
        this.saleId = saleId;
        this.cashier = cashier;
    }

    public void addItem(Product product, int quantity) {
        if (isCompleted) {
            throw new IllegalStateException("Cannot add items to a completed sale");
        }

        int currentQuantity = items.getOrDefault(product, 0);
        items.put(product, currentQuantity + quantity);
    }

    public void removeItem(Product product) {
        if (isCompleted) {
            throw new IllegalStateException("Cannot remove items from a completed sale");
        }

        items.remove(product);
    }

    public void updateItemQuantity(Product product, int quantity) {
        if (isCompleted) {
            throw new IllegalStateException("Cannot update items in a completed sale");
        }

        if (quantity <= 0) {
            items.remove(product);
        } else {
            items.put(product, quantity);
        }
    }

    public void completeSale(PaymentMethod paymentMethod, double totalAmount, Receipt receipt) {
        if (isCompleted) {
            throw new IllegalStateException("Sale is already completed");
        }

        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.receipt = receipt;
        this.isCompleted = true;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getSaleDateTime() {
        return saleDateTime;
    }

    public void setSaleDateTime(LocalDateTime saleDateTime) {
        this.saleDateTime = saleDateTime;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Product, Integer> items) {
        this.items = items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", saleDateTime=" + saleDateTime +
                ", cashier=" + (cashier != null ? cashier.getName() : "none") +
                ", items=" + items.size() +
                ", paymentMethod=" + paymentMethod +
                ", totalAmount=" + totalAmount +
                ", isCompleted=" + isCompleted +
                '}';
    }
} 