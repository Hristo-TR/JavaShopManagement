package com.shop.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CurrencyFormatterTest {

    @Test
    public void testFormatPositiveAmount() {
        assertEquals("$100.00", CurrencyFormatter.format(100));
    }
    
    @Test
    public void testFormatNegativeAmount() {
        assertEquals("-$50.00", CurrencyFormatter.format(-50));
    }
    
    @Test
    public void testFormatZeroAmount() {
        assertEquals("$0.00", CurrencyFormatter.format(0));
    }
    
    @Test
    public void testFormatWithDecimals() {
        assertEquals("$99.99", CurrencyFormatter.format(99.99));
    }
} 