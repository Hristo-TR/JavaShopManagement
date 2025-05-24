package com.shop.repository;

import com.shop.enums.ProductCategory;
import com.shop.model.product.Product;
import com.shop.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends BaseRepository<Product> {

    public List<Product> findByCategory(ProductCategory category) {
        List<Product> result = new ArrayList<>();
        for (Product product : entities.values()) {
            if (product.getCategory() == category) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findExpired() {
        List<Product> result = new ArrayList<>();
        for (Product product : entities.values()) {
            if (DateUtils.isExpired(product.getExpirationDate())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findSoonToExpire(int daysThreshold) {
        List<Product> result = new ArrayList<>();
        for (Product product : entities.values()) {
            if (DateUtils.isExpiringSoon(product.getExpirationDate(), daysThreshold)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> findBelowQuantity(int quantityThreshold) {
        List<Product> result = new ArrayList<>();
        for (Product product : entities.values()) {
            if (product.getQuantity() <= quantityThreshold) {
                result.add(product);
            }
        }
        return result;
    }

    @Override
    protected int getEntityId(Product product) {
        return product.getId();
    }
} 