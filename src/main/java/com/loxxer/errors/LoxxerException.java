package com.loxxer.errors;

/**
 * Loxxer General Exception Class
 */
public class LoxxerException extends Exception {
    public LoxxerException(String message) {
        super(message);
    }

    public LoxxerException(String message, Throwable error) {
        super(message, error);
    }

    @Override
    public String getMessage() {
        return "Error encountered: " + super.getMessage();
    }
}