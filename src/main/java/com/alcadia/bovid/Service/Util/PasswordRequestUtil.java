package com.alcadia.bovid.Service.Util;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author jhon peralta
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordRequestUtil {
    
    private String email;
    private String oldPassword;

    private String newPassword;
}