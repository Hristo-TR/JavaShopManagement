package com.shop.service.impl;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.product.Product;
import com.shop.model.product.ProductFactory;
import com.shop.model.store.Store;
import com.shop.repository.ProductRepository;
import com.shop.service.InventoryService;
import com.shop.utils.DateUtils;
import com.shop.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {

    private final Store store;
    private final ProductRepository productRepository;

    public InventoryServiceImpl(Store store, ProductRepository productRepository) {
        this.store = store;
        this.productRepository = productRepository;
    }

    @Override
    public Product addProduct(ProductCategory category, String name, double purchasePrice,
                              LocalDate expirationDate, int quantity) {
        ValidationUtils.validateNotNull(category, "Product category cannot be null");
        ValidationUtils.validateNotEmpty(name, "Product name cannot be empty");
        ValidationUtils.validatePositive(purchasePrice, "Purchase price must be positive");
        ValidationUtils.validateNotNull(expirationDate, "Expiration date cannot be null");
        ValidationUtils.validatePositiveOrZero(quantity, "Quantity cannot be negative");

        if (DateUtils.isExpired(expirationDate)) {
            throw new IllegalArgumentException("Cannot add expired product");
        }

        Product product = ProductFactory.createProduct(category, name, purchasePrice,
                expirationDate, quantity);
        store.addProduct(product);
        productRepository.save(product);

        return product;
    }

    @Override
    public void removeProduct(int productId) {
        ValidationUtils.validatePositive(productId, "Product ID must be positive");

        Product product = store.getProductById(productId);
        if (product != null) {
            store.getInventory().remove(productId);
            productRepository.delete(productId);
        }
    }

    @Override
    public void updateProductQuantity(int productId, int quantityChange)
            throws InsufficientQuantityException {
        ValidationUtils.validatePositive(productId, "Product ID must be positive");

        store.updateProductQuantity(productId, quantityChange);
        productRepository.save(store.getProductById(productId));
    }

    @Override
    public Product getProductById(int productId) {
        ValidationUtils.validatePositive(productId, "Product ID must be positive");
        return store.getProductById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(ProductCategory category) {
        ValidationUtils.validateNotNull(category, "Product category cannot be null");
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getExpiredProducts() {
        return productRepository.findExpired();
    }

    @Override
    public List<Product> getSoonToExpireProducts(int daysThreshold) {
        ValidationUtils.validatePositive(daysThreshold, "Days threshold must be positive");
        return productRepository.findSoonToExpire(daysThreshold);
    }

    @Override
    public List<Product> getProductsBelowThreshold(int quantityThreshold) {
        ValidationUtils.validatePositiveOrZero(quantityThreshold, "Quantity threshold cannot be negative");
        return productRepository.findBelowQuantity(quantityThreshold);
    }

    @Override
    public void checkExpirationDates() throws ExpiredProductException {
        List<Product> expiredProducts = getExpiredProducts();
        if (!expiredProducts.isEmpty()) {
            throw new ExpiredProductException("Found " + expiredProducts.size() + " expired products");
        }
    }

    @Override
    public double calculateInventoryValue() {
        return store.getInventory().values().stream()
                .mapToDouble(p -> p.getPurchasePrice() * p.getQuantity())
                .sum();
    }

    @Override
    public void loadInventory() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            store.addProduct(product);
        }
    }

    @Override
    public void saveInventory() {
        for (Product product : store.getInventory().values()) {
            productRepository.save(product);
        }
    }
} 