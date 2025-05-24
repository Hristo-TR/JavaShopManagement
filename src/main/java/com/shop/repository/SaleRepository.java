package com.shop.repository;

import com.shop.model.sales.Sale;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository extends BaseRepository<Sale> {

    public List<Sale> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : entities.values()) {
            LocalDate saleDate = sale.getSaleDateTime().toLocalDate();
            if ((saleDate.isEqual(startDate) || saleDate.isAfter(startDate)) &&
                    (saleDate.isEqual(endDate) || saleDate.isBefore(endDate))) {
                result.add(sale);
            }
        }
        return result;
    }

    @Override
    protected int getEntityId(Sale sale) {
        return sale.getSaleId();
    }
} 