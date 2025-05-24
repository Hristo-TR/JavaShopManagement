package com.shop.model.person;

import com.shop.enums.EmployeePosition;
import com.shop.enums.ProductCategory;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.store.Register;
import com.shop.model.store.Store;

import java.time.LocalDateTime;
import java.util.Map;

public class Cashier extends Employee {
    private int registerNumber;
    private Register currentRegister;

    public Cashier() {
        super();
        setPosition(EmployeePosition.CASHIER);
    }

    public Cashier(int id, String name, double monthlySalary, int registerNumber) {
        super(id, name, monthlySalary, EmployeePosition.CASHIER);
        this.registerNumber = registerNumber;
    }

    public Receipt createReceipt(Map<Product, Integer> items, Store store)
            throws InsufficientQuantityException, ExpiredProductException {
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int requestedQuantity = entry.getValue();

            if (product.isExpired()) {
                throw new ExpiredProductException(product);
            }

            if (product.getQuantity() < requestedQuantity) {
                throw new InsufficientQuantityException(product, requestedQuantity, product.getQuantity());
            }
        }

        Receipt receipt = new Receipt();
        receipt.setCashier(this);
        receipt.setDateTime(LocalDateTime.now());
        receipt.setItems(items);

        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double productPrice = product.calculateSellingPrice(
                    store.getDaysBeforeExpirationForDiscount(),
                    store.getDiscountPercentage(),
                    product.getCategory().equals(com.shop.enums.ProductCategory.FOOD) ?
                            store.getFoodMarkupPercentage() : store.getNonFoodMarkupPercentage()
            );

            total += productPrice * quantity;
            product.setQuantity(product.getQuantity() - quantity);
        }

        receipt.setTotalAmount(total);

        if (currentRegister != null) {
            currentRegister.addReceipt(receipt);
        }

        return receipt;
    }

    public void setCurrentRegister(Register register) {
        this.currentRegister = register;
        if (register != null) {
            this.registerNumber = register.getRegisterNumber();
        }
    }

    public Register getCurrentRegister() {
        return currentRegister;
    }

    @Override
    public String performDuties() {
        return "Cashier " + getName() + " is operating register #" + registerNumber;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                super.toString() +
                ", registerNumber=" + registerNumber +
                '}';
    }
} 