package com.shop.model.product;

import com.shop.enums.ProductCategory;

import java.time.LocalDate;

public class NonFoodProduct extends Product {

    public NonFoodProduct() {
        super();
        setCategory(ProductCategory.NON_FOOD);
    }

    public NonFoodProduct(int id, String name, double purchasePrice, LocalDate expirationDate, int quantity) {
        super(id, name, purchasePrice, expirationDate, quantity, ProductCategory.NON_FOOD);
    }

    @Override
    public double calculateSellingPrice(int daysBeforeDiscountApplies, double discountPercentage, double markupPercentage) {
        double basePrice = getPurchasePrice() * (1 + markupPercentage / 100);

        // Discount for expiring products
        if (daysUntilExpiration() <= daysBeforeDiscountApplies) {
            return basePrice * (1 - discountPercentage / 100);
        }

        return basePrice;
    }

    @Override
    public String toString() {
        return "NonFoodProduct{" + super.toString() + "}";
    }
} 