package com.shop.service;

import com.shop.service.impl.SalesServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shop.enums.EmployeePosition;
import com.shop.enums.PaymentMethod;
import com.shop.enums.ProductCategory;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.person.Cashier;
import com.shop.model.product.FoodProduct;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.sales.Sale;
import com.shop.model.store.Store;
import com.shop.repository.ReceiptRepository;
import com.shop.repository.SaleRepository;

public class SalesServiceImplTest {

    private SalesService salesService;
    
    @Mock
    private Store store;
    
    @Mock
    private ReceiptRepository receiptRepository;
    
    @Mock
    private SaleRepository saleRepository;
    
    private Cashier cashier;
    private Product validProduct;
    private Product expiredProduct;
    private Product lowStockProduct;
    private Receipt testReceipt;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        salesService = new SalesServiceImpl(store, receiptRepository, saleRepository);
        
        cashier = new Cashier();
        cashier.setId(1);
        cashier.setName("Test Cashier");
        cashier.setPosition(EmployeePosition.CASHIER);
        cashier.setMonthlySalary(2000.0);
        cashier.setRegisterNumber(5);
        
        validProduct = new FoodProduct();
        validProduct.setId(1);
        validProduct.setName("Valid Product");
        validProduct.setCategory(ProductCategory.FOOD);
        validProduct.setPurchasePrice(10.0);
        validProduct.setQuantity(100);
        validProduct.setExpirationDate(LocalDate.now().plusDays(30));
        
        expiredProduct = new FoodProduct();
        expiredProduct.setId(2);
        expiredProduct.setName("Expired Product");
        expiredProduct.setCategory(ProductCategory.FOOD);
        expiredProduct.setPurchasePrice(5.0);
        expiredProduct.setQuantity(50);
        expiredProduct.setExpirationDate(LocalDate.now().minusDays(1));
        
        lowStockProduct = new FoodProduct();
        lowStockProduct.setId(3);
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setCategory(ProductCategory.FOOD);
        lowStockProduct.setPurchasePrice(15.0);
        lowStockProduct.setQuantity(5);
        lowStockProduct.setExpirationDate(LocalDate.now().plusDays(60));
        
        when(store.getProductById(1)).thenReturn(validProduct);
        when(store.getProductById(2)).thenReturn(expiredProduct);
        when(store.getProductById(3)).thenReturn(lowStockProduct);
        
        testReceipt = new Receipt();
        testReceipt.setReceiptNumber(123);
        testReceipt.setCashier(cashier);
        testReceipt.setDateTime(LocalDateTime.now());
        testReceipt.setTotalAmount(24.0);
        
        when(saleRepository.getNextId()).thenReturn(1);
    }
    
    @Test
    public void testCreateSale() {
        Sale sale = salesService.createSale(cashier);
        
        assertNotNull(sale);
        assertEquals(cashier, sale.getCashier());
        assertEquals(0, sale.getItems().size());
        assertNotNull(sale.getSaleDateTime());
    }
    
    @Test
    public void testAddItemToSale() {
        Sale sale = salesService.createSale(cashier);
        
        salesService.addItemToSale(sale, validProduct, 2);
        
        assertEquals(1, sale.getItems().size());
        assertTrue(sale.getItems().containsKey(validProduct));
        assertEquals(Integer.valueOf(2), sale.getItems().get(validProduct));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddItemToSaleWithInvalidQuantity() {
        Sale sale = salesService.createSale(cashier);
        validProduct.setQuantity(0);
        salesService.addItemToSale(sale, validProduct, 1);
    }
    
    @Test
    public void testCompleteSale() throws InsufficientQuantityException, ExpiredProductException {
        // Setup
        Sale sale = salesService.createSale(cashier);
        
        Map<Product, Integer> items = new HashMap<>();
        items.put(validProduct, 2);
        sale.setItems(items);
        
        when(store.processSale(any(), any())).thenReturn(testReceipt);
        
        // Test
        Receipt receipt = salesService.completeSale(sale, PaymentMethod.CASH);
        
        // Verify
        assertNotNull(receipt);
        assertEquals(sale.getCashier(), receipt.getCashier());
        assertEquals(PaymentMethod.CASH, sale.getPaymentMethod());
        
        verify(receiptRepository, times(1)).save(receipt);
    }
    
    @Test(expected = InsufficientQuantityException.class)
    public void testCompleteSaleInsufficientQuantity() throws InsufficientQuantityException, ExpiredProductException {
        Sale sale = salesService.createSale(cashier);
        
        Map<Product, Integer> items = new HashMap<>();
        items.put(lowStockProduct, 10);
        sale.setItems(items);
        
        when(store.processSale(any(), any())).thenThrow(new InsufficientQuantityException("Not enough stock"));
        
        salesService.completeSale(sale, PaymentMethod.CREDIT_CARD);
    }
    
    @Test(expected = ExpiredProductException.class)
    public void testCompleteSaleExpiredProduct() throws InsufficientQuantityException, ExpiredProductException {
        Sale sale = salesService.createSale(cashier);
        
        Map<Product, Integer> items = new HashMap<>();
        items.put(expiredProduct, 1);
        sale.setItems(items);
        
        when(store.processSale(any(), any())).thenThrow(new ExpiredProductException("Product is expired"));
        
        salesService.completeSale(sale, PaymentMethod.DEBIT_CARD);
    }
    
    @Test
    public void testGetReceiptsByDateRange() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        List<Receipt> mockReceipts = new ArrayList<>();
        mockReceipts.add(new Receipt());
        mockReceipts.add(new Receipt());
        
        when(receiptRepository.findByDateRange(startDate, endDate)).thenReturn(mockReceipts);
        
        List<Receipt> result = salesService.getReceiptsByDateRange(startDate, endDate);
        
        assertEquals(2, result.size());
        verify(receiptRepository, times(1)).findByDateRange(startDate, endDate);
    }
    
    @Test
    public void testGetTotalSalesByDateRange() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        Receipt receipt1 = new Receipt();
        receipt1.setTotalAmount(100.0);
        
        Receipt receipt2 = new Receipt();
        receipt2.setTotalAmount(150.0);
        
        List<Receipt> mockReceipts = new ArrayList<>();
        mockReceipts.add(receipt1);
        mockReceipts.add(receipt2);
        
        when(receiptRepository.findByDateRange(startDate, endDate)).thenReturn(mockReceipts);
        
        double totalSales = salesService.getTotalSalesByDateRange(startDate, endDate);
        
        assertEquals(250.0, totalSales, 0.001);
    }
} 