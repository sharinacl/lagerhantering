package se.yrgo.exception;

public class InvalidTransactionException extends Exception {
    public InvalidTransactionException(String message) {
        super(message);
    }

    // Optional: Add constructor with cause
    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}