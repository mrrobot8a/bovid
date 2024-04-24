package com.alcadia.bovid.Models.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Anotación para ignorar propiedades desconocidas durante la deserialización JSON
@JsonIgnoreProperties(ignoreUnknown = true)
// Anotación para incluir propiedades que no son nulas durante la serialización JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private List<RoleDto> roles;
    private Boolean enabled;
    private String numberPhone;
    private String position;

}
