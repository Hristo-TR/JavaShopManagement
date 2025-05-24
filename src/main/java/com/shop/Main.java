package com.shop;

import com.shop.enums.EmployeePosition;
import com.shop.enums.ProductCategory;
import com.shop.model.person.Cashier;
import com.shop.model.person.Manager;
import com.shop.model.store.Store;
import com.shop.repository.EmployeeRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.ReceiptRepository;
import com.shop.repository.SaleRepository;
import com.shop.service.*;
import com.shop.service.impl.*;
import com.shop.ui.ConsoleUI;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Shop Management System");

        // Initialize store
        Store store = new Store("My Shop", 20.0, 15.0, 5, 30.0);

        ProductRepository productRepository = new ProductRepository();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        ReceiptRepository receiptRepository = new ReceiptRepository();
        SaleRepository saleRepository = new SaleRepository();

        InventoryService inventoryService = new InventoryServiceImpl(store, productRepository);
        EmployeeService employeeService = new EmployeeServiceImpl(store, employeeRepository);
        SalesService salesService = new SalesServiceImpl(store, receiptRepository, saleRepository);
        PricingService pricingService = new PricingServiceImpl(store);
        ReportingService reportingService = new ReportingServiceImpl(store, inventoryService, employeeService);

        initializeSampleData(store, inventoryService, employeeService);

        ConsoleUI ui = new ConsoleUI(store, inventoryService, employeeService, salesService, pricingService, reportingService);
        ui.start();
    }

    private static void initializeSampleData(Store store, InventoryService inventoryService, EmployeeService employeeService) {
        Cashier cashier1 = (Cashier) employeeService.addEmployee("Hristo Trifonov", 15000.0, EmployeePosition.CASHIER);
        employeeService.assignCashierToRegister(cashier1.getId(), 1);

        Cashier cashier2 = (Cashier) employeeService.addEmployee("Ivan Ivanov", 1600.0, EmployeePosition.CASHIER);
        employeeService.assignCashierToRegister(cashier2.getId(), 2);

        Manager manager = (Manager) employeeService.addEmployee("Petar", 2500.0, EmployeePosition.MANAGER);
        employeeService.assignManagerToDepartment(manager.getId(), "General");


        LocalDate today = LocalDate.now();
        inventoryService.addProduct(ProductCategory.FOOD, "Milk", 2.5, today.plusDays(7), 50);
        inventoryService.addProduct(ProductCategory.FOOD, "Bread", 1.8, today.plusDays(5), 30);
        inventoryService.addProduct(ProductCategory.FOOD, "Eggs", 3.2, today.plusDays(14), 20);
        inventoryService.addProduct(ProductCategory.FOOD, "Cheese", 5.5, today.plusDays(30), 15);
        inventoryService.addProduct(ProductCategory.FOOD, "Chocolate", 1.2, today.plusDays(10), 40);
        inventoryService.addProduct(ProductCategory.FOOD, "Jack Daniels Honey Syrup Chocolate", 10.5, today.plusDays(99), 5);

        inventoryService.addProduct(ProductCategory.FOOD, "Old Bread", 1.8, today.plusDays(1), 10);

        inventoryService.addProduct(ProductCategory.NON_FOOD, "Soap", 3.0, today.plusMonths(12), 23);
        inventoryService.addProduct(ProductCategory.NON_FOOD, "Shampoo", 4.5, today.plusMonths(18), 20);
        inventoryService.addProduct(ProductCategory.NON_FOOD, "Toothpaste", 2.8, today.plusMonths(24), 30);
        inventoryService.addProduct(ProductCategory.NON_FOOD, "Keyboard", 6.0, today.plusMonths(36), 11);
        inventoryService.addProduct(ProductCategory.NON_FOOD, "Audeze LCD-2C", 1500.0, today.plusMonths(24), 1);
    }
} 