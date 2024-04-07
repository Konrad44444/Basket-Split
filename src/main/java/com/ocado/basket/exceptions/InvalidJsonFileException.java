package com.ocado.basket.exceptions;

public class InvalidJsonFileException extends RuntimeException {
    public InvalidJsonFileException(String message, Throwable error) {
        super(message, error);
    }
}
