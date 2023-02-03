package com.loxxer.exceptions;

/**
 * Loxxer General Exception Class
 */
public class LoxxerException extends Exception {
    public LoxxerException(String message, Throwable error) {
        super(message, error);
    }
}