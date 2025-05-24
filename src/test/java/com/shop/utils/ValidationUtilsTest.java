package com.shop.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilsTest {

    @Test
    public void testValidateNotNull_ValidCase() {
        ValidationUtils.validateNotNull("Not null", "This should not throw");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNotNull_InvalidCase() {
        ValidationUtils.validateNotNull(null, "Object cannot be null");
    }
    
    @Test
    public void testValidateNotEmpty_ValidCase() {
        ValidationUtils.validateNotEmpty("Not empty", "This should not throw");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNotEmpty_NullCase() {
        ValidationUtils.validateNotEmpty(null, "String cannot be null");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateNotEmpty_EmptyCase() {
        ValidationUtils.validateNotEmpty("", "String cannot be empty");
    }
    
    @Test
    public void testValidatePositive_ValidCase() {
        ValidationUtils.validatePositive(10, "This should not throw");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidatePositive_ZeroCase() {
        ValidationUtils.validatePositive(0, "Value must be positive");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidatePositive_NegativeCase() {
        ValidationUtils.validatePositive(-10, "Value must be positive");
    }
    
    @Test
    public void testValidatePositiveOrZero_ValidCases() {
        ValidationUtils.validatePositiveOrZero(10, "This should not throw");
        ValidationUtils.validatePositiveOrZero(0, "This should not throw");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidatePositiveOrZero_NegativeCase() {
        ValidationUtils.validatePositiveOrZero(-10, "Value cannot be negative");
    }
} 