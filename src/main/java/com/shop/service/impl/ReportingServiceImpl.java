package com.shop.service.impl;

import com.shop.model.sales.Receipt;
import com.shop.model.sales.SalesReport;
import com.shop.model.store.FinancialReport;
import com.shop.model.store.Store;
import com.shop.service.EmployeeService;
import com.shop.service.InventoryService;
import com.shop.service.ReportingService;
import com.shop.utils.CurrencyFormatter;
import com.shop.utils.DateUtils;
import com.shop.utils.FileUtils;
import com.shop.utils.ValidationUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportingServiceImpl implements ReportingService {

    private static final String REPORTS_DIRECTORY = "reports";

    private final Store store;
    private final InventoryService inventoryService;
    private final EmployeeService employeeService;
    private final Map<String, String> reportCache;
    private final List<String> receiptTexts;

    public ReportingServiceImpl(Store store,
                                InventoryService inventoryService,
                                EmployeeService employeeService) {
        this.store = store;
        this.inventoryService = inventoryService;
        this.employeeService = employeeService;
        this.reportCache = new HashMap<>();
        this.receiptTexts = new ArrayList<>();

        try {
            FileUtils.createDirectory(REPORTS_DIRECTORY);
        } catch (IOException e) {
            System.err.println("Failed to create reports directory: " + e.getMessage());
        }
    }

    @Override
    public SalesReport generateSalesReport(LocalDate startDate, LocalDate endDate) {
        ValidationUtils.validateNotNull(startDate, "Start date cannot be null");
        ValidationUtils.validateNotNull(endDate, "End date cannot be null");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        SalesReport report = new SalesReport();
        report.generateReport(store, startDate, endDate);
        return report;
    }

    @Override
    public FinancialReport generateFinancialReport(LocalDate startDate, LocalDate endDate) {
        ValidationUtils.validateNotNull(startDate, "Start date cannot be null");
        ValidationUtils.validateNotNull(endDate, "End date cannot be null");

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        FinancialReport report = new FinancialReport();
        report.generateReport(store, startDate, endDate);
        return report;
    }

    @Override
    public void saveReportToMemory(SalesReport report, String reportName) {
        ValidationUtils.validateNotNull(report, "Report cannot be null");
        ValidationUtils.validateNotEmpty(reportName, "Report name cannot be empty");

        String reportText = report.generateReportText(store);
        reportCache.put(reportName, reportText);

        saveReportToFile(reportName, reportText);
    }

    @Override
    public void saveReportToMemory(FinancialReport report, String reportName) {
        ValidationUtils.validateNotNull(report, "Report cannot be null");
        ValidationUtils.validateNotEmpty(reportName, "Report name cannot be empty");

        String reportText = report.generateReportText();
        reportCache.put(reportName, reportText);

        saveReportToFile(reportName, reportText);
    }

    @Override
    public void saveReportToFile(String reportName, String reportText) {
        try {
            String filePath = REPORTS_DIRECTORY + "/" + reportName + ".txt";
            FileUtils.writeToFile(filePath, reportText);
        } catch (IOException e) {
            System.err.println("Failed to save report to file: " + e.getMessage());
        }
    }

    @Override
    public void saveReceiptToMemory(Receipt receipt) {
        ValidationUtils.validateNotNull(receipt, "Receipt cannot be null");

        String receiptText = receipt.generateReceiptText();
        receiptTexts.add(receiptText);

        String receiptName = "receipt_" + receipt.getReceiptNumber();
        reportCache.put(receiptName, receiptText);

        try {
            String filePath = REPORTS_DIRECTORY + "/" + receiptName + ".txt";
            FileUtils.writeToFile(filePath, receiptText);
        } catch (IOException e) {
            System.err.println("Failed to save receipt to file: " + e.getMessage());
        }
    }

    @Override
    public String getReportFromMemory(String reportName) {
        ValidationUtils.validateNotEmpty(reportName, "Report name cannot be empty");

        String report = reportCache.get(reportName);

        if (report == null && FileUtils.fileExists(REPORTS_DIRECTORY + "/" + reportName + ".txt")) {
            try {
                report = FileUtils.readFromFile(REPORTS_DIRECTORY + "/" + reportName + ".txt");
                reportCache.put(reportName, report);
            } catch (IOException e) {
                System.err.println("Failed to read report from file: " + e.getMessage());
            }
        }

        return report;
    }

    @Override
    public List<String> getAllReceiptTexts() {
        return new ArrayList<>(receiptTexts);
    }

    @Override
    public void generateDailySalesReport() {
        LocalDate today = LocalDate.now();
        SalesReport report = generateSalesReport(today, today);
        String reportName = "sales_report_" + DateUtils.formatDate(today);
        saveReportToMemory(report, reportName);
    }

    @Override
    public void generateMonthlyFinancialReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        FinancialReport report = generateFinancialReport(startOfMonth, endOfMonth);
        String reportName = "financial_report_" + today.getYear() + "_" + today.getMonthValue();
        saveReportToMemory(report, reportName);
    }

    @Override
    public void generateInventoryReport() {
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("======================================\n");
        reportBuilder.append("           INVENTORY REPORT           \n");
        reportBuilder.append("======================================\n");
        reportBuilder.append("Date: ").append(DateUtils.formatDate(LocalDate.now())).append("\n");
        reportBuilder.append("--------------------------------------\n");
        reportBuilder.append("Total Products: ").append(inventoryService.getAllProducts().size()).append("\n");
        reportBuilder.append("Total Inventory Value: ").append(CurrencyFormatter.format(inventoryService.calculateInventoryValue())).append("\n");
        reportBuilder.append("--------------------------------------\n");

        reportBuilder.append("Expired Products: ").append(inventoryService.getExpiredProducts().size()).append("\n");
        reportBuilder.append("Soon to Expire Products (7 days): ").append(inventoryService.getSoonToExpireProducts(7).size()).append("\n");
        reportBuilder.append("Products Below Threshold (5 units): ").append(inventoryService.getProductsBelowThreshold(5).size()).append("\n");

        String reportName = "inventory_report_" + DateUtils.formatDate(LocalDate.now());
        reportCache.put(reportName, reportBuilder.toString());

        saveReportToFile(reportName, reportBuilder.toString());
    }

    @Override
    public void generateEmployeeReport() {
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append("======================================\n");
        reportBuilder.append("           EMPLOYEE REPORT            \n");
        reportBuilder.append("======================================\n");
        reportBuilder.append("Date: ").append(DateUtils.formatDate(LocalDate.now())).append("\n");
        reportBuilder.append("--------------------------------------\n");
        reportBuilder.append("Total Employees: ").append(employeeService.getAllEmployees().size()).append("\n");
        reportBuilder.append("Total Cashiers: ").append(employeeService.getAllCashiers().size()).append("\n");
        reportBuilder.append("Total Managers: ").append(employeeService.getAllManagers().size()).append("\n");
        reportBuilder.append("--------------------------------------\n");
        reportBuilder.append("Total Monthly Salary: ").append(CurrencyFormatter.format(employeeService.calculateTotalSalaries())).append("\n");

        String reportName = "employee_report_" + DateUtils.formatDate(LocalDate.now());
        reportCache.put(reportName, reportBuilder.toString());

        saveReportToFile(reportName, reportBuilder.toString());
    }

    @Override
    public List<String> getAvailableReports() {
        List<String> reports = new ArrayList<>(reportCache.keySet());

        try {
            List<String> fileReports = FileUtils.listFiles(REPORTS_DIRECTORY);
            for (String fileReport : fileReports) {
                if (fileReport.endsWith(".txt")) {
                    String reportName = fileReport.substring(0, fileReport.length() - 4);
                    if (!reportCache.containsKey(reportName)) {
                        reports.add(reportName);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read reports from directory: " + e.getMessage());
        }

        return reports;
    }
} 