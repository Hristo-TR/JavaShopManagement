package com.shop.model.product;

import com.shop.enums.ProductCategory;

import java.time.LocalDate;

public class ProductFactory {

    private static int nextId = 1;

    public static Product createProduct(ProductCategory category, String name,
                                        double purchasePrice, LocalDate expirationDate,
                                        int quantity) {

        int id = nextId++;

        return switch (category) {
            case FOOD -> new FoodProduct(id, name, purchasePrice, expirationDate, quantity);
            case NON_FOOD -> new NonFoodProduct(id, name, purchasePrice, expirationDate, quantity);
        };
    }

    public static Product createProductWithId(int id, ProductCategory category, String name,
                                              double purchasePrice, LocalDate expirationDate,
                                              int quantity) {

        return switch (category) {
            case FOOD -> new FoodProduct(id, name, purchasePrice, expirationDate, quantity);
            case NON_FOOD -> new NonFoodProduct(id, name, purchasePrice, expirationDate, quantity);
        };
    }

    public static void setNextId(int id) {
        nextId = id;
    }
} 