package com.alcadia.bovid.Exception;

import org.springframework.http.HttpStatusCode;

/**
 * @author Sampson Alfred
 */

public class InvalidVerificationTokenException extends RuntimeException {
    public InvalidVerificationTokenException(String message, Throwable cause,HttpStatusCode status) {
        super(message);
    }
}