package com.alcadia.bovid.Exception;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {

    public CustomAccessDeniedException(String msg) {
        super(msg);
    }
}
