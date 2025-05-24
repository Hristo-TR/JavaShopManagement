package com.shop.ui;

import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.person.Manager;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.store.Store;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OutputPrinter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void printHeader(String title) {
        System.out.println("\n======================================");
        System.out.println("            " + title);
        System.out.println("======================================");
    }

    public void printSeparator() {
        System.out.println("--------------------------------------");
    }

    public void printMainMenu() {
        printHeader("MAIN MENU");
        System.out.println("1. Manage Inventory");
        System.out.println("2. Manage Employees");
        System.out.println("3. Manage Sales");
        System.out.println("4. Generate Reports");
        System.out.println("5. Display Store Information");
        System.out.println("0. Exit");
        printSeparator();
    }

    public void printInventoryMenu() {
        printHeader("INVENTORY MANAGEMENT");
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Update Product Quantity");
        System.out.println("4. Remove Product");
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }

    public void printEmployeeMenu() {
        printHeader("EMPLOYEE MANAGEMENT");
        System.out.println("1. View All Employees");
        System.out.println("2. Add New Employee");
        System.out.println("3. Update Employee Salary");
        System.out.println("4. Remove Employee");
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }

    public void printSalesMenu() {
        printHeader("SALES MANAGEMENT");
        System.out.println("1. Create New Sale");
        System.out.println("2. View Sales History");
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }

    public void printReportsMenu() {
        printHeader("REPORTS");
        System.out.println("1. Generate Daily Sales Report");
        System.out.println("2. Generate Monthly Financial Report");
        System.out.println("3. Generate Inventory Report");
        System.out.println("4. Generate Employee Report");
        System.out.println("5. View Reports");
        System.out.println("0. Back to Main Menu");
        printSeparator();
    }

    public void printProducts(List<Product> products, double[] sellingPrices) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        printHeader("PRODUCTS");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            double sellingPrice = sellingPrices[i];

            System.out.printf("ID: %d, Name: %s, Category: %s, Purchase Price: %.2f, Selling Price: %.2f, Expires: %s, Quantity: %d\n",
                    product.getId(), product.getName(), product.getCategory(),
                    product.getPurchasePrice(), sellingPrice,
                    product.getExpirationDate().format(DATE_FORMATTER),
                    product.getQuantity());
        }
    }

    public void printEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        printHeader("EMPLOYEES");
        for (Employee employee : employees) {
            String details = "";
            if (employee instanceof Cashier cashier) {
                details = "Register: " + cashier.getRegisterNumber();
            } else if (employee instanceof Manager manager) {
                details = "Department: " + manager.getDepartment();
            }

            System.out.printf("ID: %d, Name: %s, Position: %s, Salary: %.2f, %s\n",
                    employee.getId(), employee.getName(), employee.getPosition(),
                    employee.getMonthlySalary(), details);
        }
    }

    public void printReceipts(List<Receipt> receipts, double totalSales, LocalDate startDate, LocalDate endDate) {
        if (receipts.isEmpty()) {
            System.out.println("No receipts found.");
            return;
        }

        System.out.println("\nSales from " + startDate.format(DATE_FORMATTER) +
                " to " + endDate.format(DATE_FORMATTER));
        System.out.println("Total Sales: $" + String.format("%.2f", totalSales));
        System.out.println("Number of Receipts: " + receipts.size());
    }

    public void printReceiptDetails(Receipt receipt) {
        System.out.println("\n" + receipt.generateReceiptText());
    }

    public void printStoreInfo(Store store, int productCount, int employeeCount, double inventoryValue, double totalSalaries) {
        printHeader("STORE INFORMATION");
        System.out.println("Name: " + store.getName());
        System.out.println("Food Markup: " + store.getFoodMarkupPercentage() + "%");
        System.out.println("Non-Food Markup: " + store.getNonFoodMarkupPercentage() + "%");
        System.out.println("Days Before Discount: " + store.getDaysBeforeExpirationForDiscount());
        System.out.println("Discount Percentage: " + store.getDiscountPercentage() + "%");
        printSeparator();
        System.out.println("Total Products: " + productCount);
        System.out.println("Total Employees: " + employeeCount);
        System.out.println("Total Inventory Value: $" + String.format("%.2f", inventoryValue));
        System.out.println("Total Monthly Salary Expenses: $" + String.format("%.2f", totalSalaries));
        printSeparator();
    }

    public void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public void printError(String message) {
        System.out.println("✗ Error: " + message);
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void printOptions(String title, List<String> options) {
        System.out.println("\n===== " + title + " =====");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
    }
} 