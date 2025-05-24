package com.shop.service.impl;

import com.shop.enums.EmployeePosition;
import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.person.Manager;
import com.shop.model.store.Store;
import com.shop.repository.EmployeeRepository;
import com.shop.service.EmployeeService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final Store store;
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(Store store, EmployeeRepository employeeRepository) {
        this.store = store;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee addEmployee(String name, double monthlySalary, EmployeePosition position) {
        int id = employeeRepository.getNextId();
        Employee employee = switch (position) {
            case CASHIER -> new Cashier(id, name, monthlySalary, 0);
            case MANAGER -> new Manager(id, name, monthlySalary, "General");
        };

        store.addEmployee(employee);
        employeeRepository.save(employee);

        return employee;
    }

    @Override
    public Cashier addCashier(String name, double monthlySalary, int registerNumber) {
        int id = employeeRepository.getNextId();
        Cashier cashier = new Cashier(id, name, monthlySalary, registerNumber);

        store.addEmployee(cashier);
        employeeRepository.save(cashier);

        return cashier;
    }

    @Override
    public Manager addManager(String name, double monthlySalary, String department) {
        int id = employeeRepository.getNextId();
        Manager manager = new Manager(id, name, monthlySalary, department);

        store.addEmployee(manager);
        employeeRepository.save(manager);

        return manager;
    }

    @Override
    public void removeEmployee(int employeeId) {
        store.removeEmployee(employeeId);
        employeeRepository.delete(employeeId);
    }

    @Override
    public void assignCashierToRegister(int cashierId, int registerNumber) {
        Employee employee = store.getEmployeeById(cashierId);
        if (employee instanceof Cashier cashier) {
            store.assignCashierToRegister(cashier, registerNumber);
            cashier.setCurrentRegister(store.getRegister(registerNumber));
            employeeRepository.save(cashier);
        } else {
            throw new IllegalArgumentException("Employee is not a cashier");
        }
    }

    @Override
    public void assignManagerToDepartment(int managerId, String department) {
        Employee employee = store.getEmployeeById(managerId);

        if (employee instanceof Manager manager) {
            manager.setDepartment(department);
            employeeRepository.save(manager);
        } else {
            throw new IllegalArgumentException("Employee with ID " + managerId + " is not a manager");
        }
    }

    @Override
    public void updateSalary(int employeeId, double newSalary) {
        Employee employee = store.getEmployeeById(employeeId);

        if (employee != null) {
            employee.setMonthlySalary(newSalary);
            employeeRepository.save(employee);
        } else {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " not found");
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        return store.getEmployeeById(employeeId);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Cashier> getAllCashiers() {
        return employeeRepository.findByPosition(EmployeePosition.CASHIER)
                .stream()
                .filter(e -> e instanceof Cashier)
                .map(e -> (Cashier) e)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Manager> getAllManagers() {
        return employeeRepository.findByPosition(EmployeePosition.MANAGER)
                .stream()
                .filter(e -> e instanceof Manager)
                .map(e -> (Manager) e)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public double calculateTotalSalaries() {
        return store.getEmployees().stream()
                .mapToDouble(Employee::getMonthlySalary)
                .sum();
    }
} 