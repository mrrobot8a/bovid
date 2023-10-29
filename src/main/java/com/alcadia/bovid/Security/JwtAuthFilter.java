package com.alcadia.bovid.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alcadia.bovid.Exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

/**
 * Filtro que valida si la peticion tiene la cabezera de Autorizacion
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper getObjectMapper;

    /**
     * Lista blanca de URIs
     */
    private List<String> urlsToSkip = List.of("/auth", "/auth/", "/swagger-ui.html", "/swagger-ui", "/api-docs");

    /**
     * Verifica si a la URI no se le debe aplicar el filtro
     * 
     * @param request current HTTP request Petición a validar
     * @return True la URI existe en la lista blanca, false de lo contrario
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println("en esta peticion se rompe");
        System.out.println(request.getRequestURI());
        System.out.println(!request.getRequestURI().equals("/auth/sign-out"));

        if ("/auth/sign-out".equals(request.getRequestURI())) {
            return false;
        }

        return urlsToSkip.stream().anyMatch(url -> request.getRequestURI().contains(url));
    }

    /**
     * Valida si la petición contiene la cabezera de authorization con el bearer
     * token
     * 
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @throws UnauthorizedException - Si no tiene la cabezera
     *                               HttpHeaders.AUTHORIZATION
     *                               - Si tiene más de dos elementos en al cabezera
     *                               o no tiene 'Bearer'
     *                               - Si el token no es valido
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        System.out.println("=======header +  jwtauth" + header);

        // if (header == null) {
        // throw new UnauthorizedException("No tiene los permisos necesarios del
        // header");
        // }
        if (header.isEmpty() || !header.startsWith("Bearer ") || header.split(" ").length != 2) {
            filterChain.doFilter(request, response);
            return;
        }

        // validacion para token expirado
        String isTokenExpiredException = jwtService.isTokenExpired(header.split(" ")[1]);

        if (isTokenExpiredException != null) {

            responseHandler(response, isTokenExpiredException, HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        // validacion para firma del token
        String isTokenInvalidateFirma = jwtService.validatefirma(header.split(" ")[1]);

        if (isTokenInvalidateFirma != null) {

            responseHandler(response, isTokenInvalidateFirma, HttpServletResponse.SC_FORBIDDEN);

            return;
        } 
        
        System.out.println("aquiiiiiiiiii validate to list");
        String validatetokeninlist = jwtAuthenticationProvider.validatetokenInlistToken(header.split(" ")[1]);
         System.out.println("aquiiiiiiiiii validate to list" + validatetokeninlist);
         if(  validatetokeninlist !=null){
             
               responseHandler(response,validatetokeninlist, HttpServletResponse.SC_FORBIDDEN);
               return;
        }


        try {

             
            Authentication auth = jwtAuthenticationProvider.createAuthentication(header.split(" ")[1]);

            System.out.println("llegooo hasta autothentication salida de validatetoken" + auth);

            SecurityContextHolder.getContext().setAuthentication(auth);

            System.out.println("voy a imprimir el context");
            System.out.println(SecurityContextHolder.getContext());
            System.out.println("voy a imprimir la autenticacion");
            System.out.println(SecurityContextHolder.getContext().getAuthentication());

        } catch (RuntimeException e) {
            SecurityContextHolder.clearContext();
            System.out.println("se estalló");
            System.out.println(e);
            throw new RuntimeException(e);
        }
        System.out.println("llegué aqui dofilter");

        filterChain.doFilter(request, response);
    }

    /**
     * @param response
     * @param ExceptionHandler
     * @param status
     * @throws IOException
     * @throws JsonProcessingException
     * @apiNote Metodo encargado de enviar la respuesta al cliente cuando exixste
     *          una exception o validacion de token
     */
    private void responseHandler(HttpServletResponse response, String ExceptionHandler, int status) throws IOException {

        String message = getResponseJson(ExceptionHandler);

        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    /**
     * @param isTokenExpired
     * @return
     * @throws JsonProcessingException
     * @apiNote Metodo encargado de crear el json de respuesta
     */
    private String getResponseJson(String isTokenExpired)
            throws JsonProcessingException {

        Map<String, Object> jsonresponse = new HashMap<>();

        jsonresponse.put("mensaje", isTokenExpired);
        jsonresponse.put("statusCode", HttpServletResponse.SC_FORBIDDEN);
        jsonresponse.put("error", "Token no valido");

        String responseJson = getObjectMapper.writeValueAsString(jsonresponse);

        return responseJson;
    }
}
