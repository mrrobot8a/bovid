package com.alcadia.bovid.Service.UserCase;

import java.util.Map;



import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;

public interface IAuthenticationService {

   Map<String, Object> signIn(RegistrationRequest userRegistrationRequest ,  HttpServletRequest servletRequest ) throws JsonProcessingException;

    String signOut(String jwt);

    Map<String, Object> checkStatusUser(String jwt) throws JsonProcessingException ;

}
