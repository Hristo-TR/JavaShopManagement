package com.shop.service;

import com.shop.service.impl.EmployeeServiceImpl;
import com.shop.service.impl.InventoryServiceImpl;
import com.shop.service.impl.ReportingServiceImpl;
import com.shop.service.impl.SalesServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.shop.model.person.Cashier;
import com.shop.model.person.Employee;
import com.shop.model.person.Manager;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.sales.SalesReport;
import com.shop.model.store.FinancialReport;
import com.shop.model.store.Store;

public class ReportingServiceImplTest {

    private ReportingService reportingService;
    
    @Mock
    private Store store;
    
    @Mock
    private SalesServiceImpl salesServiceImpl;
    
    @Mock
    private InventoryServiceImpl inventoryServiceImpl;
    
    @Mock
    private EmployeeServiceImpl employeeServiceImpl;
    
    @Mock
    private Receipt mockReceipt;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reportingService = new ReportingServiceImpl(store, inventoryServiceImpl, employeeServiceImpl);
        
        when(mockReceipt.getReceiptNumber()).thenReturn(123);
        when(mockReceipt.generateReceiptText()).thenReturn("Mock Receipt Text");
    }
    
    @Test
    public void testGenerateSalesReport() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        SalesReport result = reportingService.generateSalesReport(startDate, endDate);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGenerateFinancialReport() {
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        
        FinancialReport result = reportingService.generateFinancialReport(startDate, endDate);
        
        assertNotNull(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSalesReportWithInvalidDateRange() {
        LocalDate endDate = LocalDate.now().minusDays(30);
        LocalDate startDate = LocalDate.now();
        
        reportingService.generateSalesReport(startDate, endDate);
    }
    
    @Test
    public void testSaveReceiptToMemory() {
        reportingService.saveReceiptToMemory(mockReceipt);
        
        List<String> receipts = reportingService.getAllReceiptTexts();
        assertEquals(1, receipts.size());
        assertEquals("Mock Receipt Text", receipts.get(0));
    }
    
    @Test
    public void testGetReportFromMemory() {
        SalesReport mockReport = mock(SalesReport.class);
        when(mockReport.generateReportText(store)).thenReturn("Test Report Text");
        
        reportingService.saveReportToMemory(mockReport, "test_report");
        
        String report = reportingService.getReportFromMemory("test_report");
        
        assertNotNull(report);
        assertEquals("Test Report Text", report);
    }
    
    @Test
    public void testGenerateInventoryReport() {
        // Setup
        List<Product> allProducts = new ArrayList<>();
        List<Product> expiredProducts = new ArrayList<>();
        List<Product> soonToExpireProducts = new ArrayList<>();
        List<Product> lowStockProducts = new ArrayList<>();
        
        when(inventoryServiceImpl.getAllProducts()).thenReturn(allProducts);
        when(inventoryServiceImpl.getExpiredProducts()).thenReturn(expiredProducts);
        when(inventoryServiceImpl.getSoonToExpireProducts(7)).thenReturn(soonToExpireProducts);
        when(inventoryServiceImpl.getProductsBelowThreshold(5)).thenReturn(lowStockProducts);
        when(inventoryServiceImpl.calculateInventoryValue()).thenReturn(1000.0);
        
        reportingService.generateInventoryReport();
        
        // Verify
        verify(inventoryServiceImpl).getAllProducts();
        verify(inventoryServiceImpl).getExpiredProducts();
        verify(inventoryServiceImpl).getSoonToExpireProducts(7);
        verify(inventoryServiceImpl).getProductsBelowThreshold(5);
        verify(inventoryServiceImpl).calculateInventoryValue();
    }
    
    @Test
    public void testGenerateEmployeeReport() {
        // Setup
        List<Employee> allEmployees = new ArrayList<>();
        List<Cashier> cashiers = new ArrayList<>();
        List<Manager> managers = new ArrayList<>();
        
        when(employeeServiceImpl.getAllEmployees()).thenReturn(allEmployees);
        when(employeeServiceImpl.getAllCashiers()).thenReturn(cashiers);
        when(employeeServiceImpl.getAllManagers()).thenReturn(managers);
        when(employeeServiceImpl.calculateTotalSalaries()).thenReturn(5000.0);
        
        reportingService.generateEmployeeReport();
        
        // Verify
        verify(employeeServiceImpl).getAllEmployees();
        verify(employeeServiceImpl).getAllCashiers();
        verify(employeeServiceImpl).getAllManagers();
        verify(employeeServiceImpl).calculateTotalSalaries();
    }
    
    @Test
    public void testGetAvailableReports() {
        SalesReport mockReport1 = mock(SalesReport.class);
        SalesReport mockReport2 = mock(SalesReport.class);
        
        when(mockReport1.generateReportText(store)).thenReturn("Report 1");
        when(mockReport2.generateReportText(store)).thenReturn("Report 2");
        
        reportingService.saveReportToMemory(mockReport1, "report1");
        reportingService.saveReportToMemory(mockReport2, "report2");
        
        List<String> reports = reportingService.getAvailableReports();
        
        assertTrue(reports.contains("report1"));
        assertTrue(reports.contains("report2"));
    }
} 