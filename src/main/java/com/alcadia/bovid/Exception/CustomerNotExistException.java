package com.alcadia.bovid.Exception;

import org.springframework.http.HttpStatus;

public class CustomerNotExistException extends RuntimeException {

    public CustomerNotExistException(String message) {
        super(message);
    }

    public CustomerNotExistException(String message, HttpStatus httpStatus) {
        super(message);
    }
}
