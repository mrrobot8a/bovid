package com.alcadia.bovid.Exception;

import java.time.Instant;

import com.auth0.jwt.exceptions.TokenExpiredException;



public class TokenExp extends TokenExpiredException {

    public TokenExp(String message, Instant expiredOn) {
        super(message, expiredOn);
        //TODO Auto-generated constructor stub
    }

   
}
