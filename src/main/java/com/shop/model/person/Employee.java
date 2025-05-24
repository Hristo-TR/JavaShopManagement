package com.shop.model.person;

import com.shop.enums.EmployeePosition;

import java.io.Serializable;

public abstract class Employee implements Serializable {
    private int id;
    private String name;
    private double monthlySalary;
    private EmployeePosition position;

    public Employee() {
    }

    public Employee(int id, String name, double monthlySalary, EmployeePosition position) {
        this.id = id;
        this.name = name;
        this.monthlySalary = monthlySalary;
        this.position = position;
    }

    public abstract String performDuties();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(double monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", monthlySalary=" + monthlySalary +
                ", position=" + position +
                '}';
    }
} 