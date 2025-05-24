package com.shop.service;

import com.shop.service.impl.EmployeeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import com.shop.enums.EmployeePosition;
import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.person.Manager;
import com.shop.model.store.Store;
import com.shop.repository.EmployeeRepository;

public class EmployeeServiceImplTest {

    private EmployeeService employeeService;
    
    @Mock
    private Store store;
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeService = new EmployeeServiceImpl(store, employeeRepository);
        
        when(employeeRepository.getNextId()).thenReturn(1);
    }
    
    @Test
    public void testAddEmployee() {
        Employee employee = employeeService.addEmployee("Hristo", 2000.0, EmployeePosition.CASHIER);
        
        assertNotNull(employee);
        assertEquals("Hristo", employee.getName());
        assertEquals(2000.0, employee.getMonthlySalary(), 0.001);
        assertEquals(EmployeePosition.CASHIER, employee.getPosition());
        
        verify(store, times(1)).addEmployee(employee);
        verify(employeeRepository, times(1)).save(employee);
    }
    
    @Test
    public void testAddCashier() {
        Cashier cashier = employeeService.addCashier("Ivan", 1800.0, 5);
        
        assertNotNull(cashier);
        assertEquals("Ivan", cashier.getName());
        assertEquals(1800.0, cashier.getMonthlySalary(), 0.001);
        assertEquals(EmployeePosition.CASHIER, cashier.getPosition());
        assertEquals(5, cashier.getRegisterNumber());
        
        verify(store, times(1)).addEmployee(cashier);
        verify(employeeRepository, times(1)).save(cashier);
    }
    
    @Test
    public void testAddManager() {
        Manager manager = employeeService.addManager("Petar", 3000.0, "Sales");
        
        assertNotNull(manager);
        assertEquals("Petar", manager.getName());
        assertEquals(3000.0, manager.getMonthlySalary(), 0.001);
        assertEquals(EmployeePosition.MANAGER, manager.getPosition());
        assertEquals("Sales", manager.getDepartment());
        
        verify(store, times(1)).addEmployee(manager);
        verify(employeeRepository, times(1)).save(manager);
    }
    
    @Test
    public void testRemoveEmployee() {
        employeeService.removeEmployee(1);
        
        verify(store, times(1)).removeEmployee(1);
        verify(employeeRepository, times(1)).delete(1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAssignCashierToRegisterWithNonCashier() {
        Manager manager = new Manager();
        manager.setId(2);
        manager.setName("Manager");
        manager.setPosition(EmployeePosition.MANAGER);
        
        when(store.getEmployeeById(2)).thenReturn(manager);
        
        employeeService.assignCashierToRegister(2, 5);
    }
    
    @Test
    public void testAssignManagerToDepartment() {
        Manager manager = new Manager();
        manager.setId(2);
        manager.setName("Manager");
        manager.setPosition(EmployeePosition.MANAGER);
        manager.setDepartment("General");
        
        when(store.getEmployeeById(2)).thenReturn(manager);
        
        employeeService.assignManagerToDepartment(2, "Finance");
        
        assertEquals("Finance", manager.getDepartment());
        verify(employeeRepository, times(1)).save(manager);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAssignManagerToDepartmentWithNonManager() {
        Cashier cashier = new Cashier();
        cashier.setId(1);
        cashier.setName("Cashier");
        cashier.setPosition(EmployeePosition.CASHIER);
        
        when(store.getEmployeeById(1)).thenReturn(cashier);
        
        employeeService.assignManagerToDepartment(1, "Finance");
    }
    
    @Test
    public void testUpdateSalary() {
        Cashier employee = new Cashier();
        employee.setId(1);
        employee.setName("Employee");
        employee.setMonthlySalary(2000.0);
        
        when(store.getEmployeeById(1)).thenReturn(employee);
        
        employeeService.updateSalary(1, 2500.0);
        
        assertEquals(2500.0, employee.getMonthlySalary(), 0.001);
        verify(employeeRepository, times(1)).save(employee);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateSalaryWithNonExistentEmployee() {
        when(store.getEmployeeById(999)).thenReturn(null);
        
        employeeService.updateSalary(999, 2500.0);
    }
    
    @Test
    public void testGetAllEmployees() {
        // Setup
        List<Employee> employees = new ArrayList<>();
        employees.add(new Cashier());
        employees.add(new Manager());
        
        when(employeeRepository.findAll()).thenReturn(employees);
        
        // Test
        List<Employee> result = employeeService.getAllEmployees();
        
        // Verify
        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }
    
    @Test
    public void testGetAllCashiers() {
        // Setup
        List<Employee> cashiers = new ArrayList<>();
        cashiers.add(new Cashier());
        
        when(employeeRepository.findByPosition(EmployeePosition.CASHIER)).thenReturn(cashiers);
        
        // Test
        List<Cashier> result = employeeService.getAllCashiers();
        
        // Verify
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findByPosition(EmployeePosition.CASHIER);
    }
    
    @Test
    public void testGetAllManagers() {
        // Setup
        List<Employee> managers = new ArrayList<>();
        managers.add(new Manager());
        
        when(employeeRepository.findByPosition(EmployeePosition.MANAGER)).thenReturn(managers);
        
        // Test
        List<Manager> result = employeeService.getAllManagers();
        
        // Verify
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findByPosition(EmployeePosition.MANAGER);
    }
    
    @Test
    public void testCalculateTotalSalaries() {
        // Setup
        List<Employee> employees = new ArrayList<>();
        
        Employee emp1 = new Cashier();
        emp1.setMonthlySalary(2000.0);
        
        Employee emp2 = new Manager();
        emp2.setMonthlySalary(3000.0);
        
        employees.add(emp1);
        employees.add(emp2);
        
        when(store.getEmployees()).thenReturn(employees);
        
        // Test
        double totalSalary = employeeService.calculateTotalSalaries();
        
        // Verify
        assertEquals(5000.0, totalSalary, 0.001);
    }
} 