package com.shop.exceptions;

public class InvalidPriceException extends Exception {
    private static final long serialVersionUID = 1L;

    private double invalidPrice;

    public InvalidPriceException(String message) {
        super(message);
    }

    public InvalidPriceException(double invalidPrice) {
        super("Invalid price value: " + invalidPrice);
        this.invalidPrice = invalidPrice;
    }

    public InvalidPriceException(String message, double invalidPrice) {
        super(message);
        this.invalidPrice = invalidPrice;
    }

    public double getInvalidPrice() {
        return invalidPrice;
    }
} 