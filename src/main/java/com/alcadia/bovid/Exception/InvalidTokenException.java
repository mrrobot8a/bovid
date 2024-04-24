package com.alcadia.bovid.Exception;

public class InvalidTokenException extends RuntimeException {

    private ErrorMessage errorMessage;

    public InvalidTokenException(ErrorMessage errorMessage) {
        super(errorMessage.getErrormessage());
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}

