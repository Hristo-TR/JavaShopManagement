package com.shop.model.sales;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.shop.enums.EmployeePosition;
import com.shop.enums.PaymentMethod;
import com.shop.enums.ProductCategory;
import com.shop.model.person.Cashier;
import com.shop.model.product.FoodProduct;
import com.shop.model.product.Product;

public class ReceiptTest {

    private Receipt receipt;
    private Cashier cashier;
    private Product product1;
    private Product product2;
    private Map<Product, Integer> items;
    private double totalAmount;
    
    @Before
    public void setUp() {
        cashier = new Cashier();
        cashier.setId(1);
        cashier.setName("Cashier");
        cashier.setPosition(EmployeePosition.CASHIER);
        cashier.setRegisterNumber(5);

        product1 = new FoodProduct();
        product1.setId(1);
        product1.setName("Apple");
        product1.setCategory(ProductCategory.FOOD);
        product1.setPurchasePrice(2.0);
        
        product2 = new FoodProduct();
        product2.setId(2);
        product2.setName("Banana");
        product2.setCategory(ProductCategory.FOOD);
        product2.setPurchasePrice(1.5);
        
        items = new HashMap<>();
        items.put(product1, 3);
        items.put(product2, 2);
        
        totalAmount = (3 * 2.0) + (2 * 1.5);

        receipt = new Receipt();
        receipt.setReceiptNumber(1001);
        receipt.setCashier(cashier);
        receipt.setDateTime(LocalDateTime.now());
        receipt.setItems(items);
        receipt.setTotalAmount(totalAmount);
    }
    
    @Test
    public void testGettersAndSetters() {
        assertEquals(1001, receipt.getReceiptNumber());
        assertEquals(cashier, receipt.getCashier());
        assertNotNull(receipt.getDateTime());
        assertEquals(items, receipt.getItems());
        assertEquals(totalAmount, receipt.getTotalAmount(), 0.001);
    }
    
    @Test
    public void testManuallyCalculatedTotal() {
        assertEquals(9.0, totalAmount, 0.001);
    }
    
    @Test
    public void testGenerateReceiptText() {
        String receiptText = receipt.generateReceiptText();
        
        assertTrue(receiptText.contains("Receipt Number: 1001"));
        assertTrue(receiptText.contains("Cashier: Cashier"));
        assertTrue(receiptText.contains("Apple"));
        assertTrue(receiptText.contains("Banana"));
        assertTrue(receiptText.contains(String.format("%.2f", totalAmount)));
    }
    
    @Test
    public void testItemsCount() {
        assertEquals(2, receipt.getItems().size());
        assertTrue(receipt.getItems().containsKey(product1));
        assertTrue(receipt.getItems().containsKey(product2));
        assertEquals(Integer.valueOf(3), receipt.getItems().get(product1));
        assertEquals(Integer.valueOf(2), receipt.getItems().get(product2));
    }
} 