package com.alcadia.bovid.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alcadia.bovid.Exception.UnauthorizedException;

import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * Filtro que valida si la peticion tiene la cabezera de Autorizacion
 */
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * Lista blanca de URIs
     */
    private List<String> urlsToSkip = List.of("/auth", "/swagger-ui.html", "/swagger-ui", "/api-docs",
            "/auth/register");

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
        /*
         * System.out.println("headers");
         * 
         * 
         * System.out.println(header);
         * System.out.println(request);
         * 
         * 
         * Enumeration<String> headerNames = request.getHeaderNames();
         * while (headerNames.hasMoreElements()) {
         * String headerName = headerNames.nextElement();
         * String headerValue = request.getHeader(headerName);
         * System.out.println("header and value");
         * System.out.println(headerName);
         * System.out.println(headerValue);
         * }
         */

        System.out.println(header);

        if (header == null) {
            throw new UnauthorizedException("No tiene los permisos necesarios del header");
        }

        String[] authElements = header.split(" ");
        System.out.println("llegooo hatas el baerer");
        if (authElements.length != 2 || !"Bearer".equals(authElements[0])) {
            throw new UnauthorizedException("No tiene los permisos necesarios.");
        }

        try {

            Authentication auth = jwtAuthenticationProvider.validateToken(authElements[1]);
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
}
