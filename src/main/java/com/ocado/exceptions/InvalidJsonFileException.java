package com.ocado.exceptions;

public class InvalidJsonFileException extends RuntimeException {
    public InvalidJsonFileException(String message, Throwable error) {
        super(message, error);
    }
}
