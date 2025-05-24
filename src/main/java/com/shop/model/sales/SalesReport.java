package com.shop.model.sales;

import com.shop.model.person.Cashier;
import com.shop.model.product.Product;
import com.shop.model.store.Store;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime generatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalSales;
    private int totalTransactions;
    private Map<Integer, Double> salesByCashier;
    private Map<Integer, Integer> itemsSoldByProduct;
    private Map<LocalDate, Double> salesByDate;

    public SalesReport() {
        this.generatedAt = LocalDateTime.now();
        this.salesByCashier = new HashMap<>();
        this.itemsSoldByProduct = new HashMap<>();
        this.salesByDate = new HashMap<>();
    }

    public void generateReport(Store store, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedAt = LocalDateTime.now();

        totalSales = 0;
        totalTransactions = 0;
        salesByCashier.clear();
        itemsSoldByProduct.clear();
        salesByDate.clear();

        List<Receipt> receiptsInPeriod = store.getReceipts().stream()
                .filter(receipt -> {
                    LocalDate receiptDate = receipt.getDateTime().toLocalDate();
                    return (receiptDate.isEqual(startDate) || receiptDate.isAfter(startDate)) &&
                            (receiptDate.isEqual(endDate) || receiptDate.isBefore(endDate));
                })
                .collect(Collectors.toList());

        for (Receipt receipt : receiptsInPeriod) {
            // Total sales and transactions
            totalSales += receipt.getTotalAmount();
            totalTransactions++;

            // Sales by cashier
            int cashierId = receipt.getCashier().getId();
            salesByCashier.put(cashierId, salesByCashier.getOrDefault(cashierId, 0.0) + receipt.getTotalAmount());

            // Items sold by product
            for (Map.Entry<Product, Integer> entry : receipt.getItems().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                itemsSoldByProduct.put(product.getId(),
                        itemsSoldByProduct.getOrDefault(product.getId(), 0) + quantity);
            }

            // Sales by date
            LocalDate receiptDate = receipt.getDateTime().toLocalDate();
            salesByDate.put(receiptDate,
                    salesByDate.getOrDefault(receiptDate, 0.0) + receipt.getTotalAmount());
        }
    }

    public String generateReportText(Store store) {
        StringBuilder sb = new StringBuilder();

        sb.append("======================================\n");
        sb.append("            SALES REPORT             \n");
        sb.append("======================================\n");
        sb.append("Generated at: ").append(generatedAt).append("\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n");
        sb.append("--------------------------------------\n");
        sb.append(String.format("Total Sales: %.2f\n", totalSales));
        sb.append("Total Transactions: ").append(totalTransactions).append("\n");

        if (totalTransactions > 0) {
            sb.append(String.format("Average Transaction Value: %.2f\n", totalSales / totalTransactions));
        }

        sb.append("--------------------------------------\n");
        sb.append("Sales by Cashier:\n");

        for (Map.Entry<Integer, Double> entry : salesByCashier.entrySet()) {
            int cashierId = entry.getKey();
            double sales = entry.getValue();
            Cashier cashier = (Cashier) store.getEmployeeById(cashierId);
            String cashierName = cashier != null ? cashier.getName() : "Unknown";

            sb.append(String.format("  %s (ID: %d): %.2f\n", cashierName, cashierId, sales));
        }

        sb.append("--------------------------------------\n");
        sb.append("Top Selling Products:\n");

        // Sort by quantity sold
        List<Map.Entry<Integer, Integer>> sortedProducts = new ArrayList<>(itemsSoldByProduct.entrySet());
        sortedProducts.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        int count = 0;
        for (Map.Entry<Integer, Integer> entry : sortedProducts) {
            if (count >= 5) break; // Show top 5

            int productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = store.getProductById(productId);
            String productName = product != null ? product.getName() : "Unknown";

            sb.append(String.format("  %s (ID: %d): %d units\n", productName, productId, quantity));
            count++;
        }

        sb.append("--------------------------------------\n");
        sb.append("Sales by Date:\n");

        List<LocalDate> dates = new ArrayList<>(salesByDate.keySet());
        dates.sort(LocalDate::compareTo);

        for (LocalDate date : dates) {
            sb.append(String.format("  %s: %.2f\n", date, salesByDate.get(date)));
        }

        sb.append("======================================\n");

        return sb.toString();
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Map<Integer, Double> getSalesByCashier() {
        return salesByCashier;
    }

    public void setSalesByCashier(Map<Integer, Double> salesByCashier) {
        this.salesByCashier = salesByCashier;
    }

    public Map<Integer, Integer> getItemsSoldByProduct() {
        return itemsSoldByProduct;
    }

    public void setItemsSoldByProduct(Map<Integer, Integer> itemsSoldByProduct) {
        this.itemsSoldByProduct = itemsSoldByProduct;
    }

    public Map<LocalDate, Double> getSalesByDate() {
        return salesByDate;
    }

    public void setSalesByDate(Map<LocalDate, Double> salesByDate) {
        this.salesByDate = salesByDate;
    }
} 