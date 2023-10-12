package com.alcadia.bovid.Service.UserCase;

import jakarta.servlet.http.HttpServletRequest;

public interface IRequestService {

     String getClientIp(HttpServletRequest request);
    
}
