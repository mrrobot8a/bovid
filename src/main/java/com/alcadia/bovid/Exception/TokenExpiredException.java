package com.alcadia.bovid.Exception;

public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message, Throwable expiredOn) {
        super(message, expiredOn);

    }

}