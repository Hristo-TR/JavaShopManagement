package com.shop.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class DateUtilsTest {

    @Test
    public void testFormatDate() {
        LocalDate date = LocalDate.of(2023, 5, 15);
        assertEquals("2023-05-15", DateUtils.formatDate(date));
    }
    
    @Test
    public void testFormatDateWithNull() {
        assertEquals("", DateUtils.formatDate(null));
    }
    
    @Test
    public void testIsExpired() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalDate futureDate = LocalDate.now().plusDays(1);
        
        assertTrue(DateUtils.isExpired(pastDate));
        assertFalse(DateUtils.isExpired(futureDate));
    }
    
    @Test
    public void testIsExpiringSoon() {
        LocalDate expiredDate = LocalDate.now().minusDays(1);
        LocalDate expiringSoonDate = LocalDate.now().plusDays(3);
        LocalDate notExpiringSoonDate = LocalDate.now().plusDays(10);
        
        assertFalse(DateUtils.isExpiringSoon(expiredDate, 5));
        assertTrue(DateUtils.isExpiringSoon(expiringSoonDate, 5));
        assertFalse(DateUtils.isExpiringSoon(notExpiringSoonDate, 5));
    }
} 