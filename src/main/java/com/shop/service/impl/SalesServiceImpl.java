package com.shop.service.impl;

import com.shop.enums.PaymentMethod;
import com.shop.exceptions.ExpiredProductException;
import com.shop.exceptions.InsufficientQuantityException;
import com.shop.model.person.Cashier;
import com.shop.model.product.Product;
import com.shop.model.sales.Receipt;
import com.shop.model.sales.Sale;
import com.shop.model.store.Store;
import com.shop.repository.ReceiptRepository;
import com.shop.repository.SaleRepository;
import com.shop.service.SalesService;

import java.time.LocalDate;
import java.util.List;

public class SalesServiceImpl implements SalesService {

    private final Store store;
    private final ReceiptRepository receiptRepository;
    private final SaleRepository saleRepository;

    public SalesServiceImpl(Store store, ReceiptRepository receiptRepository, SaleRepository saleRepository) {
        this.store = store;
        this.receiptRepository = receiptRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale createSale(Cashier cashier) {
        int saleId = saleRepository.getNextId();
        Sale sale = new Sale(saleId, cashier);
        return sale;
    }

    @Override
    public void addItemToSale(Sale sale, Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        sale.addItem(product, quantity);
    }

    @Override
    public void removeItemFromSale(Sale sale, Product product) {
        sale.removeItem(product);
    }

    @Override
    public Receipt completeSale(Sale sale, PaymentMethod paymentMethod)
            throws InsufficientQuantityException, ExpiredProductException {

        Receipt receipt = store.processSale(sale.getItems(), sale.getCashier());

        sale.completeSale(paymentMethod, receipt.getTotalAmount(), receipt);

        receiptRepository.save(receipt);
        saleRepository.save(sale);

        return receipt;
    }

    @Override
    public List<Receipt> getReceiptsByDateRange(LocalDate startDate, LocalDate endDate) {
        return receiptRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public Receipt getReceiptById(int receiptId) {
        return receiptRepository.findById(receiptId);
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return saleRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public int getTotalReceiptCount() {
        return receiptRepository.count();
    }

    @Override
    public double getTotalSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return getReceiptsByDateRange(startDate, endDate).stream()
                .mapToDouble(Receipt::getTotalAmount)
                .sum();
    }

    @Override
    public void saveReceipt(Receipt receipt) {
        receiptRepository.save(receipt);
    }
} 