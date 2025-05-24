package com.shop.model.sales;

import com.shop.model.person.Cashier;
import com.shop.model.product.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private int receiptNumber;
    private Cashier cashier;
    private LocalDateTime dateTime;
    private Map<Product, Integer> items;
    private double totalAmount;

    public Receipt() {
        this.items = new HashMap<>();
    }

    public Receipt(int receiptNumber, Cashier cashier, LocalDateTime dateTime) {
        this();
        this.receiptNumber = receiptNumber;
        this.cashier = cashier;
        this.dateTime = dateTime;
    }

    public String generateReceiptText() {
        StringBuilder sb = new StringBuilder();

        sb.append("======================================\n");
        sb.append("             RECEIPT                  \n");
        sb.append("======================================\n");
        sb.append("Receipt Number: ").append(receiptNumber).append("\n");
        sb.append("Date: ").append(dateTime).append("\n");
        sb.append("Cashier: ").append(cashier.getName()).append("\n");
        sb.append("--------------------------------------\n");
        sb.append("Items:\n");

        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPurchasePrice();

            sb.append(String.format("%-20s x%d %10.2f\n", product.getName(), quantity, price * quantity));
        }

        sb.append("--------------------------------------\n");
        sb.append(String.format("Total Amount: %10.2f\n", totalAmount));
        sb.append("======================================\n");

        return sb.toString();
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Product, Integer> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "receiptNumber=" + receiptNumber +
                ", cashier=" + cashier +
                ", dateTime=" + dateTime +
                ", items=" + items.size() +
                ", totalAmount=" + totalAmount +
                '}';
    }
} 