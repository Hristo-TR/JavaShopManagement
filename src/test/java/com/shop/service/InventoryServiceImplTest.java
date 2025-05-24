package com.shop.service;

import com.shop.service.impl.InventoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.product.Product;
import com.shop.model.product.FoodProduct;
import com.shop.model.store.Store;
import com.shop.repository.ProductRepository;

public class InventoryServiceImplTest {

    private InventoryService inventoryService;
    
    @Mock
    private Store store;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private Map<Integer, Product> inventoryMap;
    
    private Product testProduct;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inventoryService = new InventoryServiceImpl(store, productRepository);
        
        testProduct = new FoodProduct();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setCategory(ProductCategory.FOOD);
        testProduct.setPurchasePrice(10.0);
        testProduct.setExpirationDate(LocalDate.now().plusDays(30));
        testProduct.setQuantity(50);
        
        when(productRepository.findById(1)).thenReturn(testProduct);
        when(store.getProductById(1)).thenReturn(testProduct);
        when(store.getInventory()).thenReturn(inventoryMap);
        
        List<Product> allProducts = new ArrayList<>();
        allProducts.add(testProduct);
        when(productRepository.findAll()).thenReturn(allProducts);
    }
    
    @Test
    public void testAddProduct() {
        String name = "New Product";
        double price = 15.0;
        LocalDate expirationDate = LocalDate.now().plusDays(10);
        int quantity = 30;
        ProductCategory category = ProductCategory.FOOD;
        
        Product mockNewProduct = new FoodProduct();
        mockNewProduct.setId(2);
        mockNewProduct.setName(name);
        mockNewProduct.setCategory(category);
        mockNewProduct.setPurchasePrice(price);
        mockNewProduct.setExpirationDate(expirationDate);
        mockNewProduct.setQuantity(quantity);

        try {
            inventoryService.addProduct(category, name, price, expirationDate, quantity);
            
            verify(store, times(1)).addProduct(any(Product.class));
            verify(productRepository, times(1)).save(any(Product.class));
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetProductById() {
        Product result = inventoryService.getProductById(1);
        
        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(store, times(1)).getProductById(1);
    }
    
    @Test
    public void testGetAllProducts() {
        List<Product> result = inventoryService.getAllProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProduct, result.get(0));
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    public void testUpdateProductQuantity() throws InsufficientQuantityException {
        inventoryService.updateProductQuantity(1, 10);
        
        verify(store, times(1)).updateProductQuantity(1, 10);
        verify(productRepository, times(1)).save(testProduct);
    }
    
    @Test
    public void testRemoveProduct() {
        inventoryService.removeProduct(1);
        
        verify(store, times(1)).getProductById(1);
        verify(inventoryMap, times(1)).remove(1);
        verify(productRepository, times(1)).delete(1);
    }
} 