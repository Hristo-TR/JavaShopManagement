package com.shop.repository;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import com.shop.enums.ProductCategory;
import com.shop.model.product.Product;
import com.shop.model.product.FoodProduct;
import com.shop.model.product.NonFoodProduct;

public class ProductRepositoryTest {

    private ProductRepository repository;
    private Product foodProduct;
    private Product nonFoodProduct;
    private Product expiredProduct;
    
    @Before
    public void setUp() {
        repository = new ProductRepository();

        foodProduct = new FoodProduct();
        foodProduct.setId(1);
        foodProduct.setName("Apple");
        foodProduct.setCategory(ProductCategory.FOOD);
        foodProduct.setPurchasePrice(1.0);
        foodProduct.setQuantity(100);
        foodProduct.setExpirationDate(LocalDate.now().plusDays(10));
        
        nonFoodProduct = new NonFoodProduct();
        nonFoodProduct.setId(2);
        nonFoodProduct.setName("Not Apple");
        nonFoodProduct.setCategory(ProductCategory.NON_FOOD);
        nonFoodProduct.setPurchasePrice(5.0);
        nonFoodProduct.setQuantity(50);
        nonFoodProduct.setExpirationDate(LocalDate.now().plusYears(5));
        
        expiredProduct = new FoodProduct();
        expiredProduct.setId(3);
        expiredProduct.setName("Expired");
        expiredProduct.setCategory(ProductCategory.FOOD);
        expiredProduct.setPurchasePrice(2.0);
        expiredProduct.setQuantity(10);
        expiredProduct.setExpirationDate(LocalDate.now().minusDays(2));
        
        repository.save(foodProduct);
        repository.save(nonFoodProduct);
        repository.save(expiredProduct);
    }
    
    @Test
    public void testFindByCategory() {
        List<Product> foodProducts = repository.findByCategory(ProductCategory.FOOD);
        List<Product> nonFoodProducts = repository.findByCategory(ProductCategory.NON_FOOD);
        
        assertEquals(2, foodProducts.size());
        assertEquals(1, nonFoodProducts.size());
        assertTrue(foodProducts.contains(foodProduct));
        assertTrue(foodProducts.contains(expiredProduct));
        assertTrue(nonFoodProducts.contains(nonFoodProduct));
    }
    
    @Test
    public void testFindExpired() {
        List<Product> expired = repository.findExpired();
        
        assertEquals(1, expired.size());
        assertTrue(expired.contains(expiredProduct));
        assertFalse(expired.contains(foodProduct));
        assertFalse(expired.contains(nonFoodProduct));
    }
    
    @Test
    public void testFindSoonToExpire() {
        List<Product> soonToExpire = repository.findSoonToExpire(15);
        
        assertEquals(1, soonToExpire.size());
        assertTrue(soonToExpire.contains(foodProduct));
        assertFalse(soonToExpire.contains(expiredProduct));
        assertFalse(soonToExpire.contains(nonFoodProduct));
    }
    
    @Test
    public void testFindBelowQuantity() {
        List<Product> lowStock = repository.findBelowQuantity(20);
        
        assertEquals(1, lowStock.size());
        assertTrue(lowStock.contains(expiredProduct));
        assertFalse(lowStock.contains(foodProduct));
        assertFalse(lowStock.contains(nonFoodProduct));
    }
    
    @Test
    public void testCRUDOperations() {
        Product found = repository.findById(1);
        assertEquals(foodProduct, found);
        
        List<Product> all = repository.findAll();
        assertEquals(3, all.size());
        
        foodProduct.setQuantity(90);
        repository.save(foodProduct);
        found = repository.findById(1);
        assertEquals(90, found.getQuantity());
        
        repository.delete(3);
        assertNull(repository.findById(3));
        all = repository.findAll();
        assertEquals(2, all.size());
    }
} 