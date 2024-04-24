package com.alcadia.bovid.Component.aspects;

import org.apache.logging.log4j.LogManager;

import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.alcadia.bovid.Component.util.InfoRequestClientComponet;
import com.alcadia.bovid.Models.Dto.RegistrationRequest;
import com.alcadia.bovid.Models.Dto.UserDto;

import com.alcadia.bovid.Service.UserCase.IHistorialAuditoriaService;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.exc.StreamReadException;

import com.fasterxml.jackson.databind.DatabindException;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
public class UserActionAuditAspect {

    @Autowired
    private IHistorialAuditoriaService historialAuditoriaService;

    @Autowired
    private InfoRequestClientComponet getInfoClientComponet;

    // @AfterReturning(pointcut = "execution(*
    // your.package.controllers.*Controller.*(..))", returning = "result")
    @Pointcut("execution(* com.alcadia.bovid.Controllers.*.*(..)) && !execution(* com.alcadia.bovid.Controllers.LoginController.*(..))")
    private void logUserAction() {
    };

    @Pointcut("execution(* com.alcadia.bovid.Controllers.*.signIn(..))")
    private void logUserActionSingIn() {
    };

    @Before("logUserAction()")
    public void logUserAction(JoinPoint joinPoint) throws StreamReadException, DatabindException, IOException {
        LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName())
                .info("User action: " + joinPoint.getSignature().getName()
                        + "(---------------------------------------------------)");

        // Obtiene la información de la solicitud
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {

            HttpServletRequest request = attributes.getRequest();
            // Obtiene el método HTTP y la URL
            String httpMethod = request.getMethod();
            String url = request.getRequestURL().toString();
            String ipclient = getInfoClientComponet.getClientIp(request);
            String path = request.getServletPath();
            String actionUser = joinPoint.getSignature().getName();
            // String jwt = request.getHeader("Authorization");
            String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);

            // Obtiene el nombre del usuario (puedes personalizar cómo obtienes esto)
            UserDto username = getUserFromAuthentication(); // Debes implementar esta lógica

            // Registra la información (puedes personalizar cómo y dónde la registras)
            System.out.println("User: " + username.getEmail());
            System.out.println("HTTP Method: " + httpMethod);
            System.out.println("URL: " + url);
            System.out.println("pat: " + path);
            System.out.println("UserAction:" + joinPoint.getSignature().getName());
            System.out.println("IP: " + ipclient);
            System.out.println("Host: " + getInfoClientComponet.getHostClient(request));
            System.out.println("jwt: " + jwt);

            historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod, url, username.getEmail());

        } else {
            // Manejar el caso en el que 'attributes' es nulo
        }

    }

    @Pointcut("execution(* com.alcadia.bovid.Controllers.*.signOut(..))")
    private void loginOut() {
    };

    @AfterReturning("loginOut()")
    public void logLoginOutAction(JoinPoint joinPoint) throws StreamReadException, DatabindException, IOException {
        LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName())
                .info("User action: " + joinPoint.getSignature().getName()
                        + "(---------------------------------------------------)");
        try {

            // Obtiene la información de la solicitud
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes != null) {

                HttpServletRequest request = attributes.getRequest();
                // Obtiene el método HTTP y la URL
                String httpMethod = request.getMethod();
                String url = request.getRequestURL().toString();
                String ipclient = getInfoClientComponet.getClientIp(request);
                String path = request.getServletPath();
                String actionUser = joinPoint.getSignature().getName();
                // String jwt = request.getHeader("Authorization");
                String jwt = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1];
                String emailUser = JWT.decode(jwt).getClaim("email").asString();


                // Registra la información (puedes personalizar cómo y dónde la registras)
                System.out.println("User: " + emailUser);
                System.out.println("HTTP Method: " + httpMethod);
                System.out.println("URL: " + url);
                System.out.println("pat: " + path);
                System.out.println("UserAction:" + joinPoint.getSignature().getName());
                System.out.println("IP: " + ipclient);
                System.out.println("Host: " + getInfoClientComponet.getHostClient(request));
                System.out.println("jwt: " + jwt);
               
                historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod, url,emailUser);

            } else {
                // Manejar el caso en el que 'attributes' es nulo
            }
        } catch (Exception e) {
            LogManager.getLogger(e)
                    .info(e.getMessage()
                            + "(---------------------------------------------------)");

        }
    }

    @Pointcut("execution(* com.alcadia.bovid.Controllers.*.signIn(..)) && args(authCustomerDto, servletRequest)")
    private void loginIn(RegistrationRequest authCustomerDto, HttpServletRequest servletRequest) {
    }

    @AfterReturning(pointcut = "loginIn(authCustomerDto, servletRequest)", returning = "response")
    public void logLoginInAction(JoinPoint joinPoint, RegistrationRequest authCustomerDto,
            HttpServletRequest servletRequest, ResponseEntity<Map<String, Object>> response) {
        try {
            // Aquí puedes acceder a los parámetros y a la respuesta del controlador
            // Puedes realizar el registro o cualquier operación necesaria
            System.out.println("SignIn method called with parameters: " + authCustomerDto + ", " + servletRequest);

            System.out.println("estatus code:" + response.getStatusCode());
            System.out.println("Response: " + response.getBody());

            String httpMethod = servletRequest.getMethod();
            String url = servletRequest.getRequestURL().toString();
            String actionUser = joinPoint.getSignature().getName();

            if (response.getStatusCode() == HttpStatus.OK) {
                String ipclient = getInfoClientComponet.getClientIp(servletRequest);

                historialAuditoriaService.registerHistorial(actionUser, ipclient, httpMethod,
                        url, authCustomerDto.getEmail());

            }

        } catch (Exception e) {
            // Manejo de cualquier excepción que ocurra durante el proceso
            System.err.println("An error occurred while processing the request: " + e.getMessage());
        }
    }

    // Debes implementar la lógica para obtener el nombre del usuario desde la
    // autenticación
    private UserDto getUserFromAuthentication() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String auditor = "System";

        if (Objects.nonNull(authentication)) {

            auditor = authentication.getName();

            if (auditor.equals("anonymousUser")) {
                return UserDto.builder().firstName("anonymousUser").lastName("anonymousUser").email("anonymousUser").build();
            }
        }

        UserDto user = (UserDto) authentication.getPrincipal();

        return user;
    }

}
// @Pointcut("execution(* com.alcadia.bovid.Controllers.*.cu(..))")
// private void logUserActionSingOut() {
// };

// @AfterReturning(pointcut = "logUserActionSingOut()", returning = "result")
// private void logUserActionSingOut(JoinPoint joinPoint, Object result) {

// System.out.println("======================outttttt==============="+getUserFromAuthentication());

// }

// @Pointcut("execution(*
// com.alcadia.bovid.Security.JwtAuthenticationProvider.validateToken(..))")
// private void exceptionJtw() {
// };

// @AfterThrowing(pointcut = "exceptionJtw()", throwing = "exception")
// public void handleTokenExpiredException(JoinPoint joinPoint, Exception
// exception)
// throws IOException, ServletException {
// LogManager.getLogger(joinPoint.getSignature().getDeclaringTypeName())
// .info("User action: " + joinPoint.getSignature().getName()
// + "(---------------------" + exception.getMessage() +
// "------------------------------)");
// ServletRequestAttributes attributes = (ServletRequestAttributes)
// RequestContextHolder.getRequestAttributes();
// HttpServletRequest request = attributes.getRequest();
// HttpServletResponse response = attributes.getResponse();
// accessDeniedHandlerException.handle(request, response, new
// AccessDeniedException(exception.getMessage()));

// }

// nota : hay que cambiar los metodos 1 y 2 por como esta el 3 que es el de
// loginin