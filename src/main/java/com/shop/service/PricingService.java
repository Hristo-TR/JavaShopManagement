package com.shop.service;

import com.shop.enums.ProductCategory;
import com.shop.exceptions.InvalidPriceException;
import com.shop.model.product.Product;

import java.time.LocalDate;

public interface PricingService {
    double calculateSellingPrice(Product product);

    double calculateSellingPrice(Product product,
                                 int daysBeforeDiscountApplies,
                                 double discountPercentage,
                                 double markupPercentage);

    String formatPrice(double price);

    double getMarkupPercentage(ProductCategory category);

    void setMarkupPercentage(ProductCategory category, double markupPercentage)
            throws InvalidPriceException;

    void setDiscountParameters(int daysBeforeDiscountApplies, double discountPercentage)
            throws InvalidPriceException;

    double calculateDiscount(double originalPrice, double discountPercentage);

    double calculatePriceWithMarkup(double basePrice, double markupPercentage);

    boolean isProductEligibleForDiscount(Product product);

    double getTotalValueAfterDiscount(double originalValue, LocalDate expirationDate);
}
