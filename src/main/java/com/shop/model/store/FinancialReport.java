package com.shop.model.store;

import com.shop.model.person.Employee;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class FinancialReport implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime generatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalSales;
    private double totalPurchases;
    private double totalSalaries;
    private double totalProfit;
    private int totalReceiptsCount;
    private Map<Integer, Double> salesByRegister;

    public FinancialReport() {
        this.generatedAt = LocalDateTime.now();
        this.salesByRegister = new HashMap<>();
    }

    public void generateReport(Store store, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.generatedAt = LocalDateTime.now();

        // Calculate total sales
        totalSales = 0;
        totalReceiptsCount = 0;
        salesByRegister.clear();

        for (Receipt receipt : store.getReceipts()) {
            LocalDate receiptDate = receipt.getDateTime().toLocalDate();

            if ((receiptDate.isEqual(startDate) || receiptDate.isAfter(startDate)) &&
                    (receiptDate.isEqual(endDate) || receiptDate.isBefore(endDate))) {

                totalSales += receipt.getTotalAmount();
                totalReceiptsCount++;

                // Add to sales by register
                int registerNumber = receipt.getCashier().getRegisterNumber();
                salesByRegister.put(registerNumber,
                        salesByRegister.getOrDefault(registerNumber, 0.0) + receipt.getTotalAmount());
            }
        }

        // Calculate total purchases
        totalPurchases = 0;
        for (Product product : store.getInventory().values()) {
            totalPurchases += product.getPurchasePrice() * product.getQuantity();
        }

        // Calculate total salaries
        totalSalaries = 0;
        int months = (int) ChronoUnit.MONTHS.between(startDate, endDate) + 1;
        for (Employee employee : store.getEmployees()) {
            totalSalaries += employee.getMonthlySalary() * months;
        }

        // Calculate profit
        totalProfit = totalSales - totalPurchases - totalSalaries;
    }

    public String generateReportText() {
        StringBuilder sb = new StringBuilder();

        sb.append("======================================\n");
        sb.append("          FINANCIAL REPORT           \n");
        sb.append("======================================\n");
        sb.append("Generated at: ").append(generatedAt).append("\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n");
        sb.append("--------------------------------------\n");
        sb.append(String.format("Total Sales: %.2f\n", totalSales));
        sb.append(String.format("Total Purchases: %.2f\n", totalPurchases));
        sb.append(String.format("Total Salaries: %.2f\n", totalSalaries));
        sb.append("--------------------------------------\n");
        sb.append(String.format("Total Profit: %.2f\n", totalProfit));
        sb.append("--------------------------------------\n");
        sb.append("Total Receipts: ").append(totalReceiptsCount).append("\n");
        sb.append("Sales by Register:\n");

        for (Map.Entry<Integer, Double> entry : salesByRegister.entrySet()) {
            sb.append(String.format("  Register #%d: %.2f\n", entry.getKey(), entry.getValue()));
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

    public double getTotalPurchases() {
        return totalPurchases;
    }

    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }

    public double getTotalSalaries() {
        return totalSalaries;
    }

    public void setTotalSalaries(double totalSalaries) {
        this.totalSalaries = totalSalaries;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public int getTotalReceiptsCount() {
        return totalReceiptsCount;
    }

    public void setTotalReceiptsCount(int totalReceiptsCount) {
        this.totalReceiptsCount = totalReceiptsCount;
    }

    public Map<Integer, Double> getSalesByRegister() {
        return salesByRegister;
    }

    public void setSalesByRegister(Map<Integer, Double> salesByRegister) {
        this.salesByRegister = salesByRegister;
    }
} 