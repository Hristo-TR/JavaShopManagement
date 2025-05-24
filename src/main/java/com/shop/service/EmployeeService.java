package com.shop.service;

import com.shop.enums.EmployeePosition;
import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.person.Manager;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(String name, double monthlySalary, EmployeePosition position);

    Cashier addCashier(String name, double monthlySalary, int registerNumber);

    Manager addManager(String name, double monthlySalary, String department);

    void removeEmployee(int employeeId);

    void assignCashierToRegister(int cashierId, int registerNumber);

    void assignManagerToDepartment(int managerId, String department);

    void updateSalary(int employeeId, double newSalary);

    Employee getEmployeeById(int employeeId);

    List<Employee> getAllEmployees();

    List<Cashier> getAllCashiers();

    List<Manager> getAllManagers();

    double calculateTotalSalaries();
}
