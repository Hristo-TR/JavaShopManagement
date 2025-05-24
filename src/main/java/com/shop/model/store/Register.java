package com.shop.model.store;

import com.shop.model.person.Cashier;
import com.shop.model.sales.Receipt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Register implements Serializable {
    private static final long serialVersionUID = 1L;

    private int registerNumber;
    private Cashier currentCashier;
    private double cashAmount;
    private List<Receipt> dailyReceipts;

    public Register() {
        this.dailyReceipts = new ArrayList<>();
        this.cashAmount = 0.0;
    }

    public Register(int registerNumber) {
        this();
        this.registerNumber = registerNumber;
    }

    public void addReceipt(Receipt receipt) {
        dailyReceipts.add(receipt);
        cashAmount += receipt.getTotalAmount();
    }

    public double getDailyTotal() {
        return dailyReceipts.stream()
                .mapToDouble(Receipt::getTotalAmount)
                .sum();
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    public Cashier getCurrentCashier() {
        return currentCashier;
    }

    public void setCurrentCashier(Cashier currentCashier) {
        this.currentCashier = currentCashier;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public List<Receipt> getDailyReceipts() {
        return dailyReceipts;
    }

    public void setDailyReceipts(List<Receipt> dailyReceipts) {
        this.dailyReceipts = dailyReceipts;
    }

    @Override
    public String toString() {
        return "Register{" +
                "registerNumber=" + registerNumber +
                ", currentCashier=" + (currentCashier != null ? currentCashier.getName() : "none") +
                ", cashAmount=" + cashAmount +
                ", receiptsCount=" + dailyReceipts.size() +
                '}';
    }
} 