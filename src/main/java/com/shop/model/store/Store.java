package com.shop.model.store;

import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store implements Serializable {
    private String name;
    private List<Employee> employees;
    private Inventory inventory;
    private Map<Integer, Register> registers;
    private List<Receipt> receipts;
    private double foodMarkupPercentage;
    private double nonFoodMarkupPercentage;
    private int daysBeforeExpirationForDiscount;
    private double discountPercentage;
    private int nextReceiptNumber;

    public Store() {
        this.employees = new ArrayList<>();
        this.inventory = new Inventory();
        this.registers = new HashMap<>();
        this.receipts = new ArrayList<>();
        this.nextReceiptNumber = 1;
        initializeRegisters(5);
    }

    public Store(String name, double foodMarkupPercentage, double nonFoodMarkupPercentage,
                 int daysBeforeExpirationForDiscount, double discountPercentage) {
        this();
        this.name = name;
        this.foodMarkupPercentage = foodMarkupPercentage;
        this.nonFoodMarkupPercentage = nonFoodMarkupPercentage;
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
        this.discountPercentage = discountPercentage;
    }

    private void initializeRegisters(int count) {
        for (int i = 1; i <= count; i++) {
            registers.put(i, new Register(i));
        }
    }

    public void addProduct(Product product) {
        inventory.addProduct(product);
    }

    public void updateProductQuantity(int productId, int quantity) throws InsufficientQuantityException {
        inventory.updateQuantity(productId, quantity);
    }

    public Product getProductById(int productId) {
        return inventory.getProductById(productId);
    }

    public Map<Integer, Product> getInventory() {
        return inventory.getProducts();
    }

    public void setInventory(Map<Integer, Product> products) {
        inventory.setProducts(products);
    }

    public Register getRegister(int registerNumber) {
        return registers.get(registerNumber);
    }

    public void assignCashierToRegister(Cashier cashier, int registerNumber) {
        Register register = registers.get(registerNumber);
        if (register != null) {
            register.setCurrentCashier(cashier);
        }
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void removeEmployee(int employeeId) {
        employees.removeIf(employee -> employee.getId() == employeeId);
    }

    public Employee getEmployeeById(int employeeId) {
        for (Employee employee : employees) {
            if (employee.getId() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    public Receipt processSale(Map<Product, Integer> items, Cashier cashier)
            throws InsufficientQuantityException, ExpiredProductException {

        Receipt receipt = cashier.createReceipt(items, this);
        receipt.setReceiptNumber(nextReceiptNumber++);
        receipts.add(receipt);

        return receipt;
    }

    public double calculateExpenses() {
        double totalExpenses = 0;

        for (Employee employee : employees) {
            totalExpenses += employee.getMonthlySalary();
        }

        for (Product product : inventory.getProducts().values()) {
            totalExpenses += product.getPurchasePrice() * product.getQuantity();
        }

        return totalExpenses;
    }

    public double calculateIncome() {
        double totalIncome = 0;

        for (Receipt receipt : receipts) {
            totalIncome += receipt.getTotalAmount();
        }

        return totalIncome;
    }

    public double calculateProfit() {
        return calculateIncome() - calculateExpenses();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public double getFoodMarkupPercentage() {
        return foodMarkupPercentage;
    }

    public void setFoodMarkupPercentage(double foodMarkupPercentage) {
        this.foodMarkupPercentage = foodMarkupPercentage;
    }

    public double getNonFoodMarkupPercentage() {
        return nonFoodMarkupPercentage;
    }

    public void setNonFoodMarkupPercentage(double nonFoodMarkupPercentage) {
        this.nonFoodMarkupPercentage = nonFoodMarkupPercentage;
    }

    public int getDaysBeforeExpirationForDiscount() {
        return daysBeforeExpirationForDiscount;
    }

    public void setDaysBeforeExpirationForDiscount(int daysBeforeExpirationForDiscount) {
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getNextReceiptNumber() {
        return nextReceiptNumber;
    }

    public void setNextReceiptNumber(int nextReceiptNumber) {
        this.nextReceiptNumber = nextReceiptNumber;
    }
} 