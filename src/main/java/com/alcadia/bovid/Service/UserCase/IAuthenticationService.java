package com.alcadia.bovid.Service.UserCase;

import com.alcadia.bovid.Models.Dto.RegistrationRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

    String signIn(RegistrationRequest userRegistrationRequest ,  HttpServletRequest servletRequest );

    String signOut(String jwt);

}
