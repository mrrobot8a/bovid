package com.alcadia.bovid.Service.Util;

import lombok.Data;

/**
 * @author jhon peralta 
 */
@Data
public class PasswordRequestUtil {
    private String email;
    private String oldPassword;
    private String newPassword;
}