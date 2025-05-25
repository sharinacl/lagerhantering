package se.yrgo.exception;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
    }

    // Optional: Add constructor with cause
    public ProductNotFoundException(Long productId, Throwable cause) {
        super("Product not found with ID: " + productId, cause);
    }
}

