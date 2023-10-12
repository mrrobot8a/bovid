package com.alcadia.bovid.Exception;

public class FtpErrors extends RuntimeException {

    private ErrorMessage errorMessage;

    public FtpErrors(ErrorMessage errorMessage) {
        super(errorMessage.getErrormessage());
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
    
}
