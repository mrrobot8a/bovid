package com.alcadia.bovid.Exception;

public class CustomerNotExistException extends RuntimeException {

    public CustomerNotExistException(String message) {
        super(message);
    }
}
