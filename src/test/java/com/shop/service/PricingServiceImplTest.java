package com.shop.service;

import com.shop.service.impl.PricingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.InvalidPriceException;
import com.shop.model.product.Product;
import com.shop.model.product.FoodProduct;
import com.shop.model.product.NonFoodProduct;
import com.shop.model.store.Store;

public class PricingServiceImplTest {

    private PricingService pricingService;
    
    @Mock
    private Store store;
    
    private Product foodProduct;
    private Product nonFoodProduct;
    private Product expiringProduct;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        when(store.getFoodMarkupPercentage()).thenReturn(20.0);
        when(store.getNonFoodMarkupPercentage()).thenReturn(40.0);
        when(store.getDaysBeforeExpirationForDiscount()).thenReturn(5);
        when(store.getDiscountPercentage()).thenReturn(30.0);
        
        pricingService = new PricingServiceImpl(store);
        
        foodProduct = new FoodProduct();
        foodProduct.setId(1);
        foodProduct.setName("Apple");
        foodProduct.setCategory(ProductCategory.FOOD);
        foodProduct.setPurchasePrice(10.0);
        foodProduct.setQuantity(100);
        foodProduct.setExpirationDate(LocalDate.now().plusDays(30));
        
        nonFoodProduct = new NonFoodProduct();
        nonFoodProduct.setId(2);
        nonFoodProduct.setName("Plate");
        nonFoodProduct.setCategory(ProductCategory.NON_FOOD);
        nonFoodProduct.setPurchasePrice(20.0);
        nonFoodProduct.setQuantity(50);
        nonFoodProduct.setExpirationDate(LocalDate.now().plusYears(5));
        
        expiringProduct = new FoodProduct();
        expiringProduct.setId(3);
        expiringProduct.setName("Expiring Milk");
        expiringProduct.setCategory(ProductCategory.FOOD);
        expiringProduct.setPurchasePrice(5.0);
        expiringProduct.setQuantity(10);
        expiringProduct.setExpirationDate(LocalDate.now().plusDays(3));
    }
    
    @Test
    public void testGetMarkupPercentage() {
        assertEquals(20.0, pricingService.getMarkupPercentage(ProductCategory.FOOD), 0.001);
        assertEquals(40.0, pricingService.getMarkupPercentage(ProductCategory.NON_FOOD), 0.001);
    }
    
    @Test
    public void testCalculateSellingPrice() {
        assertEquals(12.0, pricingService.calculateSellingPrice(foodProduct), 0.001);
        
        assertEquals(28.0, pricingService.calculateSellingPrice(nonFoodProduct), 0.001);
        
        assertEquals(4.2, pricingService.calculateSellingPrice(expiringProduct), 0.001);
    }
    
    @Test
    public void testFormatPrice() {
        assertEquals("$12.34", pricingService.formatPrice(12.34));
        assertEquals("$0.99", pricingService.formatPrice(0.99));
        assertEquals("$1,234.56", pricingService.formatPrice(1234.56));
    }
    
    @Test
    public void testCalculateDiscount() {
        assertEquals(20.0, pricingService.calculateDiscount(100.0, 20.0), 0.001);
        assertEquals(5.0, pricingService.calculateDiscount(50.0, 10.0), 0.001);
    }
    
    @Test
    public void testCalculatePriceWithMarkup() {
        assertEquals(120.0, pricingService.calculatePriceWithMarkup(100.0, 20.0), 0.001);
        assertEquals(55.0, pricingService.calculatePriceWithMarkup(50.0, 10.0), 0.001);
    }
    
    @Test
    public void testIsProductEligibleForDiscount() {
        assertFalse(pricingService.isProductEligibleForDiscount(foodProduct));
        assertTrue(pricingService.isProductEligibleForDiscount(expiringProduct));
    }
    
    @Test
    public void testSetMarkupPercentage() throws InvalidPriceException {
        pricingService.setMarkupPercentage(ProductCategory.FOOD, 30.0);
        verify(store, times(1)).setFoodMarkupPercentage(30.0);
        
        pricingService.setMarkupPercentage(ProductCategory.NON_FOOD, 50.0);
        verify(store, times(1)).setNonFoodMarkupPercentage(50.0);
    }
    
    @Test(expected = InvalidPriceException.class)
    public void testSetMarkupPercentageWithNegativeValue() throws InvalidPriceException {
        pricingService.setMarkupPercentage(ProductCategory.FOOD, -10.0);
    }
    
    @Test
    public void testSetDiscountParameters() throws InvalidPriceException {
        pricingService.setDiscountParameters(7, 40.0);
        verify(store, times(1)).setDaysBeforeExpirationForDiscount(7);
        verify(store, times(1)).setDiscountPercentage(40.0);
    }
} 