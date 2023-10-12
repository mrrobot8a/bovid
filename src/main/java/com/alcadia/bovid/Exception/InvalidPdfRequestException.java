package com.alcadia.bovid.Exception;

public class InvalidPdfRequestException extends RuntimeException {
    public InvalidPdfRequestException(String message) {
        super(message);
    }
}
