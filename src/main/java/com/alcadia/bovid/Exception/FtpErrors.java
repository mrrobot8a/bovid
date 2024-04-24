package com.alcadia.bovid.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class FtpErrors extends RuntimeException {

    private ErrorMessage errorMessage;
    private HttpStatus  httpStatus;

    public FtpErrors(ErrorMessage errorMessage) {
        super(errorMessage.getErrormessage());
        this.httpStatus = errorMessage.getErrorcode();
        this.errorMessage = errorMessage;
        
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }	

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    
}
