package com.alcadia.bovid.Models.Dto;



/**
 * @author JHON PERALTA
 */

public record RegistrationRequest
(String firstName, String lastName, String email,
 String password,String role ,String codeAdmin, String numberPhone,Boolean isEnableEmail,String position) {
    // No es necesario definir explícitamente los campos ni implementar métodos.
}

