package com.shop.model.product;

import com.shop.enums.ProductCategory;

import java.time.LocalDate;

public class FoodProduct extends Product {

    public FoodProduct() {
        super();
        setCategory(ProductCategory.FOOD);
    }

    public FoodProduct(int id, String name, double purchasePrice, LocalDate expirationDate, int quantity) {
        super(id, name, purchasePrice, expirationDate, quantity, ProductCategory.FOOD);
    }

    @Override
    public double calculateSellingPrice(int daysBeforeDiscountApplies, double discountPercentage, double markupPercentage) {
        double basePrice = getPurchasePrice() * (1 + markupPercentage / 100);

        // Discount for expiringg products
        if (daysUntilExpiration() <= daysBeforeDiscountApplies) {
            return basePrice * (1 - discountPercentage / 100);
        }

        return basePrice;
    }

    @Override
    public String toString() {
        return "FoodProduct{" + super.toString() + "}";
    }
} 