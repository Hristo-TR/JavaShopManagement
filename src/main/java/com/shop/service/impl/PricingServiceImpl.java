package com.shop.service.impl;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.InvalidPriceException;
import com.shop.model.product.Product;
import com.shop.model.store.Store;
import com.shop.service.PricingService;
import com.shop.utils.CurrencyFormatter;
import com.shop.utils.ValidationUtils;

import java.time.LocalDate;

public class PricingServiceImpl implements PricingService {

    private final Store store;

    public PricingServiceImpl(Store store) {
        this.store = store;
    }

    @Override
    public double calculateSellingPrice(Product product) {
        ValidationUtils.validateNotNull(product, "Product cannot be null");
        return calculateSellingPrice(product,
                store.getDaysBeforeExpirationForDiscount(),
                store.getDiscountPercentage(),
                getMarkupPercentage(product.getCategory()));
    }

    @Override
    public double calculateSellingPrice(Product product,
                                        int daysBeforeDiscountApplies,
                                        double discountPercentage,
                                        double markupPercentage) {
        ValidationUtils.validateNotNull(product, "Product cannot be null");
        ValidationUtils.validatePositiveOrZero(daysBeforeDiscountApplies, "Days before discount cannot be negative");
        ValidationUtils.validatePositiveOrZero(discountPercentage, "Discount percentage cannot be negative");
        ValidationUtils.validatePositiveOrZero(markupPercentage, "Markup percentage cannot be negative");

        return product.calculateSellingPrice(daysBeforeDiscountApplies,
                discountPercentage,
                markupPercentage);
    }

    @Override
    public String formatPrice(double price) {
        return CurrencyFormatter.format(price);
    }

    @Override
    public double getMarkupPercentage(ProductCategory category) {
        ValidationUtils.validateNotNull(category, "Product category cannot be null");
        return switch (category) {
            case FOOD -> store.getFoodMarkupPercentage();
            case NON_FOOD -> store.getNonFoodMarkupPercentage();
        };
    }

    @Override
    public void setMarkupPercentage(ProductCategory category, double markupPercentage)
            throws InvalidPriceException {

        ValidationUtils.validateNotNull(category, "Product category cannot be null");

        if (markupPercentage < 0) {
            throw new InvalidPriceException("Markup percentage cannot be negative", markupPercentage);
        }

        switch (category) {
            case FOOD:
                store.setFoodMarkupPercentage(markupPercentage);
                break;
            case NON_FOOD:
                store.setNonFoodMarkupPercentage(markupPercentage);
                break;
            default:
                throw new IllegalArgumentException("Unknown product category: " + category);
        }
    }

    @Override
    public void setDiscountParameters(int daysBeforeDiscountApplies, double discountPercentage)
            throws InvalidPriceException {

        ValidationUtils.validatePositiveOrZero(daysBeforeDiscountApplies, "Days before discount applies cannot be negative");

        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new InvalidPriceException("Discount percentage must be between 0 and 100", discountPercentage);
        }

        store.setDaysBeforeExpirationForDiscount(daysBeforeDiscountApplies);
        store.setDiscountPercentage(discountPercentage);
    }

    @Override
    public double calculateDiscount(double originalPrice, double discountPercentage) {
        ValidationUtils.validatePositiveOrZero(originalPrice, "Original price cannot be negative");
        ValidationUtils.validatePositiveOrZero(discountPercentage, "Discount percentage cannot be negative");
        return originalPrice * (discountPercentage / 100);
    }

    @Override
    public double calculatePriceWithMarkup(double basePrice, double markupPercentage) {
        ValidationUtils.validatePositiveOrZero(basePrice, "Base price cannot be negative");
        ValidationUtils.validatePositiveOrZero(markupPercentage, "Markup percentage cannot be negative");
        return basePrice * (1 + markupPercentage / 100);
    }

    @Override
    public boolean isProductEligibleForDiscount(Product product) {
        ValidationUtils.validateNotNull(product, "Product cannot be null");

        if (product.isExpired()) {
            return false; // no expired products
        }

        return product.daysUntilExpiration() <= store.getDaysBeforeExpirationForDiscount();
    }

    @Override
    public double getTotalValueAfterDiscount(double originalValue, LocalDate expirationDate) {
        ValidationUtils.validatePositiveOrZero(originalValue, "Original value cannot be negative");
        ValidationUtils.validateNotNull(expirationDate, "Expiration date cannot be null");

        Product dummyProduct = new com.shop.model.product.FoodProduct();
        dummyProduct.setExpirationDate(expirationDate);
        dummyProduct.setPurchasePrice(originalValue);

        return calculateSellingPrice(dummyProduct);
    }
} 