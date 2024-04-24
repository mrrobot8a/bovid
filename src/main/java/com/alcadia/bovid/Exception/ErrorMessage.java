package com.alcadia.bovid.Exception;

import org.springframework.http.HttpStatus;


import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class ErrorMessage  extends RuntimeException{

    private HttpStatus errorcode;
    private String errormessage;

    public ErrorMessage(HttpStatus errorcode, String errormessage) {
        this.errorcode = errorcode;
        this.errormessage = errormessage;
    }

    public HttpStatus getErrorcode() {
        return errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorcode=" + errorcode +
                ", errormessage='" + errormessage + '\'' +
                '}';
    }

}
