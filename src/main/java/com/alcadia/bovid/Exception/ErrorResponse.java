package com.alcadia.bovid.Exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    // Getters y setters

}
