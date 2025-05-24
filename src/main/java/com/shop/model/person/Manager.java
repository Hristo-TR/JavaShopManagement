package com.shop.model.person;

import com.shop.enums.EmployeePosition;
import com.shop.model.store.Store;

public class Manager extends Employee {
    private String department;

    public Manager() {
        super();
        setPosition(EmployeePosition.MANAGER);
    }

    public Manager(int id, String name, double monthlySalary, String department) {
        super(id, name, monthlySalary, EmployeePosition.MANAGER);
        this.department = department;
    }

    public void adjustPricing(Store store, double foodMarkupPercentage, double nonFoodMarkupPercentage) {
        store.setFoodMarkupPercentage(foodMarkupPercentage);
        store.setNonFoodMarkupPercentage(nonFoodMarkupPercentage);
    }

    public void adjustDiscountPolicy(Store store, int daysBeforeExpirationForDiscount, double discountPercentage) {
        store.setDaysBeforeExpirationForDiscount(daysBeforeExpirationForDiscount);
        store.setDiscountPercentage(discountPercentage);
    }

    @Override
    public String performDuties() {
        return "Manager " + getName() + " is managing the " + department + " department";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Manager{" +
                super.toString() +
                ", department='" + department + '\'' +
                '}';
    }
} 