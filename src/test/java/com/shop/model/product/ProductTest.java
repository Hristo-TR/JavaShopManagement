package com.shop.model.product;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

import com.shop.enums.ProductCategory;

public class ProductTest {

    private FoodProduct foodProduct;
    private NonFoodProduct nonFoodProduct;
    
    @Before
    public void setUp() {
        foodProduct = new FoodProduct();
        foodProduct.setId(1);
        foodProduct.setName("Apple");
        foodProduct.setCategory(ProductCategory.FOOD);
        foodProduct.setPurchasePrice(2.50);
        foodProduct.setQuantity(100);
        foodProduct.setExpirationDate(LocalDate.now().plusDays(10));
        
        nonFoodProduct = new NonFoodProduct();
        nonFoodProduct.setId(2);
        nonFoodProduct.setName("NotApple");
        nonFoodProduct.setCategory(ProductCategory.NON_FOOD);
        nonFoodProduct.setPurchasePrice(15.0);
        nonFoodProduct.setQuantity(50);
        nonFoodProduct.setExpirationDate(LocalDate.now().plusYears(5));
    }
    
    @Test
    public void testGettersAndSetters() {
        assertEquals(1, foodProduct.getId());
        assertEquals("Apple", foodProduct.getName());
        assertEquals(ProductCategory.FOOD, foodProduct.getCategory());
        assertEquals(2.50, foodProduct.getPurchasePrice(), 0.001);
        assertEquals(100, foodProduct.getQuantity());
        assertEquals(LocalDate.now().plusDays(10).toEpochDay(), foodProduct.getExpirationDate().toEpochDay());
    }
    
    @Test
    public void testIsExpired() {
        assertFalse(foodProduct.isExpired());
        
        LocalDate yesterday = LocalDate.now().minusDays(1);
        foodProduct.setExpirationDate(yesterday);
        
        assertTrue(foodProduct.isExpired());
    }
    
    @Test
    public void testDaysUntilExpiration() {
        LocalDate expirationDate = LocalDate.now().plusDays(5);
        foodProduct.setExpirationDate(expirationDate);
        
        assertEquals(5, foodProduct.daysUntilExpiration());
        
        foodProduct.setExpirationDate(LocalDate.now().minusDays(2));
        
        assertEquals(-2, foodProduct.daysUntilExpiration());
    }
    
    @Test
    public void testCalculateSellingPrice() {
        double price = foodProduct.calculateSellingPrice(5, 10, 20);
        assertEquals(3.0, price, 0.001);
        
        foodProduct.setExpirationDate(LocalDate.now().plusDays(3));
        price = foodProduct.calculateSellingPrice(5, 10, 20);
        assertEquals(2.7, price, 0.001);
    }
} 