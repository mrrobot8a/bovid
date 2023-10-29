package com.alcadia.bovid.Exception;

/**
 * @author Sampson Alfred
 */

public class InvalidVerificationTokenException extends RuntimeException {
    public InvalidVerificationTokenException(String message, Throwable cause) {
        super(message);
    }
}