package com.shop.repository;

import com.shop.model.sales.Receipt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReceiptRepository extends BaseRepository<Receipt> {

    private int nextReceiptNumber = 1;

    public List<Receipt> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Receipt> result = new ArrayList<>();
        for (Receipt receipt : entities.values()) {
            LocalDate receiptDate = receipt.getDateTime().toLocalDate();
            if ((receiptDate.isEqual(startDate) || receiptDate.isAfter(startDate)) &&
                    (receiptDate.isEqual(endDate) || receiptDate.isBefore(endDate))) {
                result.add(receipt);
            }
        }
        return result;
    }

    public int getNextReceiptNumber() {
        return nextReceiptNumber++;
    }

    @Override
    protected int getEntityId(Receipt receipt) {
        return receipt.getReceiptNumber();
    }
} 