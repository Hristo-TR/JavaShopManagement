package com.shop.service;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.product.Product;

import java.time.LocalDate;
import java.util.List;

public interface InventoryService {
    Product addProduct(ProductCategory category, String name, double purchasePrice,
                       LocalDate expirationDate, int quantity);

    void removeProduct(int productId);

    void updateProductQuantity(int productId, int quantityChange)
            throws InsufficientQuantityException;

    Product getProductById(int productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(ProductCategory category);

    List<Product> getExpiredProducts();

    List<Product> getSoonToExpireProducts(int daysThreshold);

    List<Product> getProductsBelowThreshold(int quantityThreshold);

    void checkExpirationDates() throws ExpiredProductException;

    double calculateInventoryValue();

    void loadInventory();

    void saveInventory();
}
