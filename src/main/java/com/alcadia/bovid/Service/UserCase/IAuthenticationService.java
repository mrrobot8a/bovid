package com.alcadia.bovid.Service.UserCase;

import java.util.Map;

import com.alcadia.bovid.Models.Dto.RegistrationRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

   Map<String, Object> signIn(RegistrationRequest userRegistrationRequest ,  HttpServletRequest servletRequest );

    String signOut(String jwt);

}
