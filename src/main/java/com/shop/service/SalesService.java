package com.shop.service;

import com.shop.enums.PaymentMethod;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.person.Cashier;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.sales.Sale;

import java.time.LocalDate;
import java.util.List;

public interface SalesService {
    Sale createSale(Cashier cashier);

    void addItemToSale(Sale sale, Product product, int quantity);

    void removeItemFromSale(Sale sale, Product product);

    Receipt completeSale(Sale sale, PaymentMethod paymentMethod)
            throws InsufficientQuantityException, ExpiredProductException;

    List<Receipt> getReceiptsByDateRange(LocalDate startDate, LocalDate endDate);

    Receipt getReceiptById(int receiptId);

    List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate);

    int getTotalReceiptCount();

    double getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate);

    void saveReceipt(Receipt receipt);
}
