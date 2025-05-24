package com.shop.service;

import com.shop.model.sales.Receipt;
import com.shop.model.sales.SalesReport;
import com.shop.model.store.FinancialReport;

import java.time.LocalDate;
import java.util.List;

public interface ReportingService {
    SalesReport generateSalesReport(LocalDate startDate, LocalDate endDate);

    FinancialReport generateFinancialReport(LocalDate startDate, LocalDate endDate);

    void saveReportToMemory(SalesReport report, String reportName);

    void saveReportToMemory(FinancialReport report, String reportName);

    void saveReportToFile(String reportName, String reportText);

    void saveReceiptToMemory(Receipt receipt);

    String getReportFromMemory(String reportName);

    List<String> getAllReceiptTexts();

    void generateDailySalesReport();

    void generateMonthlyFinancialReport();

    void generateInventoryReport();

    void generateEmployeeReport();

    List<String> getAvailableReports();
}
