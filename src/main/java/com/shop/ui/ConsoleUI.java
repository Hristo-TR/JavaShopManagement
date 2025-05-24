package com.shop.ui;

import com.shop.enums.EmployeePosition;
import com.shop.enums.PaymentMethod;
import com.shop.enums.ProductCategory;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.sales.Sale;
import com.shop.model.store.Store;
import com.shop.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {

    private final Store store;
    private final InventoryService inventoryService;
    private final EmployeeService employeeService;
    private final SalesService salesService;
    private final PricingService pricingService;
    private final ReportingService reportingService;

    private final Scanner scanner;
    private final InputReader inputReader;
    private final OutputPrinter outputPrinter;
    private final MenuHandler menuHandler;

    public ConsoleUI(Store store, InventoryService inventoryService, EmployeeService employeeService,
                     SalesService salesService, PricingService pricingService, ReportingService reportingService) {
        this.store = store;
        this.inventoryService = inventoryService;
        this.employeeService = employeeService;
        this.salesService = salesService;
        this.pricingService = pricingService;
        this.reportingService = reportingService;

        this.scanner = new Scanner(System.in);
        this.inputReader = new InputReader(scanner);
        this.outputPrinter = new OutputPrinter();
        this.menuHandler = new MenuHandler(inputReader, outputPrinter);
    }

    public void start() {
        outputPrinter.printHeader("SHOP MANAGEMENT SYSTEM");
        outputPrinter.print("Sample data has been loaded.");
        menuHandler.waitForEnter();

        boolean running = true;
        while (running) {
            int choice = menuHandler.handleMainMenu();

            switch (choice) {
                case 1 -> manageInventory();
                case 2 -> manageEmployees();
                case 3 -> manageSales();
                case 4 -> generateReports();
                case 5 -> displayStoreInfo();
                case 0 -> running = false;
                default -> outputPrinter.printError("Invalid choice. Please try again.");
            }
        }

        menuHandler.exitApplication();
        scanner.close();
    }

    private void manageInventory() {
        boolean running = true;
        while (running) {
            int choice = menuHandler.handleInventoryMenu();

            switch (choice) {
                case 1 -> displayProducts();
                case 2 -> addNewProduct();
                case 3 -> updateProductQuantity();
                case 4 -> removeProduct();
                case 0 -> running = false;
                default -> outputPrinter.printError("Invalid choice. Please try again.");
            }
        }
    }

    private void displayProducts() {
        List<Product> products = inventoryService.getAllProducts();
        double[] sellingPrices = new double[products.size()];

        for (int i = 0; i < products.size(); i++) {
            sellingPrices[i] = pricingService.calculateSellingPrice(products.get(i));
        }

        outputPrinter.printProducts(products, sellingPrices);
    }

    private void addNewProduct() {
        outputPrinter.printHeader("ADD NEW PRODUCT");

        outputPrinter.print("Select Product Category:");
        outputPrinter.print("1. Food");
        outputPrinter.print("2. Non-Food");
        int categoryChoice = inputReader.readInt("Enter choice: ", 1, 2);

        ProductCategory category = (categoryChoice == 1) ? ProductCategory.FOOD : ProductCategory.NON_FOOD;

        String name = inputReader.readNonEmptyString("Enter product name: ");
        double purchasePrice = inputReader.readDouble("Enter purchase price: ", 0.01, Double.MAX_VALUE);

        outputPrinter.print("Enter expiration date (YYYY-MM-DD)");
        LocalDate expirationDate = inputReader.readDate("Enter expiration date: ", LocalDate.now(), null);

        int quantity = inputReader.readInt("Enter initial quantity: ", 0, 999999999);

        try {
            Product product = inventoryService.addProduct(category, name, purchasePrice, expirationDate, quantity);
            outputPrinter.printSuccess("Product added successfully with ID: " + product.getId());
        } catch (IllegalArgumentException e) {
            outputPrinter.printError(e.getMessage());
        }
    }

    private void updateProductQuantity() {
        outputPrinter.printHeader("UPDATE PRODUCT QUANTITY");
        displayProducts();

        int productId = inputReader.readInt("Enter product ID to update (0 to cancel): ");
        if (productId == 0) return;

        Product product = inventoryService.getProductById(productId);
        if (product == null) {
            outputPrinter.printError("Product not found.");
            return;
        }

        outputPrinter.print("Current quantity: " + product.getQuantity());
        int quantityChange = inputReader.readInt("Enter quantity change (positive to add, negative to remove): ");

        try {
            inventoryService.updateProductQuantity(productId, quantityChange);
            outputPrinter.printSuccess("Product quantity updated successfully.");
        } catch (InsufficientQuantityException e) {
            outputPrinter.printError(e.getMessage());
        }
    }

    private void removeProduct() {
        outputPrinter.printHeader("REMOVE PRODUCT");
        displayProducts();

        int productId = inputReader.readInt("Enter product ID to remove (0 to cancel): ");
        if (productId == 0) return;

        Product product = inventoryService.getProductById(productId);
        if (product == null) {
            outputPrinter.printError("Product not found.");
            return;
        }

        boolean confirm = menuHandler.confirmAction("Are you sure you want to remove " + product.getName() + "?");
        if (confirm) {
            inventoryService.removeProduct(productId);
            outputPrinter.printSuccess("Product removed successfully.");
        } else {
            outputPrinter.print("Operation cancelled.");
        }
    }

    private void manageEmployees() {
        boolean running = true;
        while (running) {
            int choice = menuHandler.handleEmployeeMenu();

            switch (choice) {
                case 1 -> displayEmployees();
                case 2 -> addNewEmployee();
                case 3 -> updateEmployeeSalary();
                case 4 -> removeEmployee();
                case 0 -> running = false;
                default -> outputPrinter.printError("Invalid choice. Please try again.");
            }
        }
    }

    private void displayEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        outputPrinter.printEmployees(employees);
    }

    private void addNewEmployee() {
        outputPrinter.printHeader("ADD NEW EMPLOYEE");

        outputPrinter.print("Select Employee Position:");
        outputPrinter.print("1. Cashier");
        outputPrinter.print("2. Manager");
        int positionChoice = inputReader.readInt("Enter choice: ", 1, 2);

        EmployeePosition position = (positionChoice == 1) ? EmployeePosition.CASHIER : EmployeePosition.MANAGER;

        String name = inputReader.readNonEmptyString("Enter employee name: ");
        double salary = inputReader.readDouble("Enter monthly salary: ", 0.01, Double.MAX_VALUE);

        try {
            Employee employee = employeeService.addEmployee(name, salary, position);
            outputPrinter.printSuccess("Employee added successfully with ID: " + employee.getId());

            if (position == EmployeePosition.CASHIER) {
                int registerNumber = inputReader.readInt("Assign to register number: ", 1, Integer.MAX_VALUE);
                employeeService.assignCashierToRegister(employee.getId(), registerNumber);
                outputPrinter.printSuccess("Cashier assigned to register " + registerNumber);
            } else if (position == EmployeePosition.MANAGER) {
                String department = inputReader.readNonEmptyString("Assign to department: ");
                employeeService.assignManagerToDepartment(employee.getId(), department);
                outputPrinter.printSuccess("Manager assigned to department: " + department);
            }

        } catch (IllegalArgumentException e) {
            outputPrinter.printError(e.getMessage());
        }
    }

    private void updateEmployeeSalary() {
        outputPrinter.printHeader("UPDATE EMPLOYEE SALARY");
        displayEmployees();

        int employeeId = inputReader.readInt("Enter employee ID to update (0 to cancel): ");
        if (employeeId == 0) return;

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            outputPrinter.printError("Employee not found.");
            return;
        }

        outputPrinter.print("Current salary: $" + String.format("%.2f", employee.getMonthlySalary()));
        double newSalary = inputReader.readDouble("Enter new salary: ", 0.01, Double.MAX_VALUE);

        try {
            employeeService.updateSalary(employeeId, newSalary);
            outputPrinter.printSuccess("Employee salary updated successfully.");
        } catch (IllegalArgumentException e) {
            outputPrinter.printError(e.getMessage());
        }
    }

    private void removeEmployee() {
        outputPrinter.printHeader("REMOVE EMPLOYEE");
        displayEmployees();

        int employeeId = inputReader.readInt("Enter employee ID to remove (0 to cancel): ");
        if (employeeId == 0) return;

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            outputPrinter.printError("Employee not found.");
            return;
        }

        boolean confirm = menuHandler.confirmAction("Are you sure you want to remove " + employee.getName() + "?");
        if (confirm) {
            employeeService.removeEmployee(employeeId);
            outputPrinter.printSuccess("Employee removed successfully.");
        } else {
            outputPrinter.print("Operation cancelled.");
        }
    }

    private void manageSales() {
        boolean running = true;
        while (running) {
            int choice = menuHandler.handleSalesMenu();

            switch (choice) {
                case 1 -> createNewSale();
                case 2 -> viewSalesHistory();
                case 0 -> running = false;
                default -> outputPrinter.printError("Invalid choice. Please try again.");
            }
        }
    }

    private void createNewSale() {
        outputPrinter.printHeader("CREATE NEW SALE");

        List<Cashier> cashiers = employeeService.getAllCashiers();
        if (cashiers.isEmpty()) {
            outputPrinter.printError("No cashiers available. Cannot create sale.");
            return;
        }

        List<String> cashierOptions = new ArrayList<>();
        for (Cashier cashier : cashiers) {
            cashierOptions.add(cashier.getName() + " (Register: " + cashier.getRegisterNumber() + ")");
        }

        outputPrinter.printOptions("Select Cashier", cashierOptions);
        int cashierIndex = inputReader.readInt("Enter cashier number: ", 1, cashiers.size()) - 1;

        Cashier selectedCashier = cashiers.get(cashierIndex);
        Sale sale = salesService.createSale(selectedCashier);

        boolean addingItems = true;
        while (addingItems) {
            outputPrinter.print("\nAvailable Products:");
            displayProducts();

            int productId = inputReader.readInt("Enter product ID to add (0 to finish): ");
            if (productId == 0) {
                addingItems = false;
                continue;
            }

            Product product = inventoryService.getProductById(productId);
            if (product == null) {
                outputPrinter.printError("Product not found.");
                continue;
            }

            int quantity = inputReader.readInt("Enter quantity: ", 1, 999999999);

            try {
                salesService.addItemToSale(sale, product, quantity);
                outputPrinter.printSuccess("Item added to sale.");
            } catch (IllegalArgumentException e) {
                outputPrinter.printError(e.getMessage());
            }
        }

        if (sale.getItems().isEmpty()) {
            outputPrinter.print("Sale cancelled - no items added.");
            return;
        }

        outputPrinter.printHeader("SALE SUMMARY");
        outputPrinter.print("Cashier: " + selectedCashier.getName());
        outputPrinter.print("Register: " + selectedCashier.getRegisterNumber());
        outputPrinter.print("Items:");

        double total = 0;
        for (Map.Entry<Product, Integer> entry : sale.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = pricingService.calculateSellingPrice(product);
            double itemTotal = price * quantity;
            total += itemTotal;

            outputPrinter.print(String.format("  %s x%d: $%.2f ($%.2f each)",
                    product.getName(), quantity, itemTotal, price));
        }

        outputPrinter.print(String.format("Total: $%.2f", total));

        outputPrinter.print("\nSelect Payment Method:");
        outputPrinter.print("1. Cash");
        outputPrinter.print("2. Credit Card");
        outputPrinter.print("3. Debit Card");

        int paymentChoice = inputReader.readInt("Enter payment method: ", 1, 3);
        PaymentMethod paymentMethod = switch (paymentChoice) {
            case 1 -> PaymentMethod.CASH;
            case 2 -> PaymentMethod.CREDIT_CARD;
            case 3 -> PaymentMethod.DEBIT_CARD;
            default -> PaymentMethod.CASH;
        };

        boolean confirm = menuHandler.confirmAction("Confirm sale?");
        if (confirm) {
            try {
                Receipt receipt = salesService.completeSale(sale, paymentMethod);

                reportingService.saveReceiptToMemory(receipt);

                outputPrinter.printHeader("RECEIPT");
                outputPrinter.print(receipt.generateReceiptText());
                outputPrinter.printSuccess("Sale completed successfully!");
            } catch (InsufficientQuantityException | ExpiredProductException e) {
                outputPrinter.printError(e.getMessage());
            }
        } else {
            outputPrinter.print("Sale cancelled.");
        }
    }

    private void viewSalesHistory() {
        outputPrinter.printHeader("SALES HISTORY");

        outputPrinter.print("Enter date range for sales history:");
        LocalDate startDate = inputReader.readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = inputReader.readDate("End date (YYYY-MM-DD): ", startDate, null);

        List<Receipt> receipts = salesService.getReceiptsByDateRange(startDate, endDate);
        double totalSales = salesService.getTotalSalesByDateRange(startDate, endDate);

        outputPrinter.printReceipts(receipts, totalSales, startDate, endDate);

        if (!receipts.isEmpty()) {
            boolean viewDetails = menuHandler.confirmAction("View detailed receipts?");
            if (viewDetails) {
                for (Receipt receipt : receipts) {
                    outputPrinter.printReceiptDetails(receipt);
                }
            }
        }
    }

    private void generateReports() {
        boolean running = true;
        while (running) {
            int choice = menuHandler.handleReportsMenu();

            switch (choice) {
                case 1 -> {
                    reportingService.generateDailySalesReport();
                    outputPrinter.printSuccess("Daily sales report generated successfully.");
                }
                case 2 -> {
                    reportingService.generateMonthlyFinancialReport();
                    outputPrinter.printSuccess("Monthly financial report generated successfully.");
                }
                case 3 -> {
                    reportingService.generateInventoryReport();
                    outputPrinter.printSuccess("Inventory report generated successfully.");
                }
                case 4 -> {
                    reportingService.generateEmployeeReport();
                    outputPrinter.printSuccess("Employee report generated successfully.");
                }
                case 5 -> viewReports();
                case 0 -> running = false;
                default -> outputPrinter.printError("Invalid choice. Please try again.");
            }
        }
    }

    private void viewReports() {
        outputPrinter.printHeader("AVAILABLE REPORTS");
        List<String> reports = reportingService.getAvailableReports();

        if (reports.isEmpty()) {
            outputPrinter.print("No reports available. Generate some reports first.");
            return;
        }

        List<String> reportOptions = new ArrayList<>(reports);
        outputPrinter.printOptions("Available Reports", reportOptions);

        int reportIndex = inputReader.readInt("Enter report number to view (0 to cancel): ", 0, reports.size());
        if (reportIndex == 0) return;

        String reportName = reports.get(reportIndex - 1);
        String reportContent = reportingService.getReportFromMemory(reportName);

        outputPrinter.printHeader("REPORT: " + reportName);
        outputPrinter.print(reportContent);
    }

    private void displayStoreInfo() {
        int productCount = inventoryService.getAllProducts().size();
        int employeeCount = employeeService.getAllEmployees().size();
        double inventoryValue = inventoryService.calculateInventoryValue();
        double totalSalaries = employeeService.calculateTotalSalaries();

        outputPrinter.printStoreInfo(store, productCount, employeeCount, inventoryValue, totalSalaries);
    }
} 