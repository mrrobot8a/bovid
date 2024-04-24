package com.alcadia.bovid.Exception;

import org.springframework.http.HttpStatus;

import org.springframework.mail.MailSendException;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RegistrationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public Map<String, String> userDataConflict(UserAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidVerificationTokenException.class)
    public Map<String, String> tokenNotFound(InvalidVerificationTokenException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoleNoExistsException.class)
    public Map<String, String> RoleNoExistsException(RoleNoExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Map<String, String> AccessDeniedException(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordIncorrectException.class)
    public Map<String, String> PasswordIncorrectException(PasswordIncorrectException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MailSendException.class)
    public Map<String, String> MessagingException(MailSendException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, String> UnauthorizedException(UnauthorizedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomerNotExistException.class)
    public Map<String, String> CustomerNotExistException(CustomerNotExistException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(handleEmailSendingFailure.class)
    public Map<String, String> handleEmailSendingFailure(handleEmailSendingFailure ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FtpErrors.class)
    public Map<String, String> FtpErrors(FtpErrors ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("statusCode", ex.getHttpStatus().toString());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPdfRequestException.class)
    public Map<String, String> handleInvalidPdfRequest(InvalidPdfRequestException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Solicitud inválida");
        error.put("message", ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenExpiredException.class)
    public Map<String, String> handleTokenExpiredRequest(TokenExpiredException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", "Solicitud inválida");
        error.put("message", ex.getMessage());
        return error;
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(InvalidVerificationTokenException.class)
    // public Map<String, String> handleInvalidVerificationTokenException(InvalidVerificationTokenException ex) {

    //     Map<String, String> error = new HashMap<>();
    //     error.put("error", "Solicitud inválida");
    //     error.put("message", ex.getMessage());
    //     return error;
    // }
    // @ExceptionHandler(JWTVerificationException.class)
    // public ResponseEntity<Object>
    // handleJWTVerificationException(JWTVerificationException ex) {
    // return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    // }

    // @ExceptionHandler(TokenExpiredException.class)
    // public ResponseEntity<Object>
    // handleTokenExpiredException(TokenExpiredException ex) {
    // return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    // }

}