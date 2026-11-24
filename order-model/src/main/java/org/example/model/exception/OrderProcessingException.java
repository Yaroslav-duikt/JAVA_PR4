package org.example.model.exception;

/**
 * Специфічний виняток для операцій із замовленнями.
 */
public class OrderProcessingException extends Exception {

    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
