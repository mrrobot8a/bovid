package com.alcadia.bovid.Security;

import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;

import com.alcadia.bovid.Exception.InvalidVerificationTokenException;
import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase encargada de la creacion y validacion de jwt para el inicio de sesion
 * de un Usuario
 */
@Slf4j
@Component
public class JwtAuthenticationProvider {

    @Autowired
    private JwtService jwtService;
    /**
     * Lista blanca con los jwt creados
     */
    private HashMap<String, UserDto> listToken = new HashMap<>();

    /**
     * Crea un nuevo jwt en base al cliente recibido por parametro y lo agrega a la
     * lista blanca
     * 
     * @param customerJwt Cliente a utilizar en la creacion del jwt
     * @return Jwt creado
     * @throws JsonProcessingException
     */
    public String createToken(UserDto customerJwt) throws JsonProcessingException {

        String tokenCreated = jwtService.createToken(customerJwt);

        listToken.put(tokenCreated, customerJwt);

        return tokenCreated;
    }

    /**
     * Valida si el token es valido y retorna una sesión del usuario
     * 
     * @param token Token a validar
     * @return Sesion del usuario
     * @throws CredentialsExpiredException Si el token ya expiró
     * @throws BadCredentialsException     Si el token no existe en la lista blanca
     */
    public Authentication createAuthentication(String token) throws AuthenticationException {

        try {
            System.out.println("entre tambien aqui validateToken");
            System.out.println(token);

            // obtener el detalle del usuario que esta en el token
            UserDto exists = listToken.get(token);

            System.out.println("============user================>" + exists);

            if (exists == null) {
                throw new BadCredentialsException("Usuario no registrado o no ha iniciadosession.");

            }

            HashSet<SimpleGrantedAuthority> rolesAndAuthorities = new HashSet<>();

            for (RoleDto role : exists.getRoles()) {
                rolesAndAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
            }

            System.out.println("=========rolesAuthenticate===========" + rolesAndAuthorities);

            // rolesAndAuthorities.add(new SimpleGrantedAuthority("ELIMINAR_PRIVILEGE")); //
            // permisos del rol

            return new UsernamePasswordAuthenticationToken(exists, token, rolesAndAuthorities);

        } catch (Exception e) {
            log.debug(token + " no valido:" + e.getMessage());
            return null;

            // throw new CustomerNotExistException("Token no valido:" + e.getMessage());
        }

    }

    public boolean validateToken(String token) throws JsonProcessingException {
        try {

            if (jwtService.validateFirma(token) != null)
                return false;
            if (validatetokenInlistToken(token) != null)
                return false;
            if (jwtService.validateToken(token))
                return false;

            return true;
            
        } catch (Exception e) {
            log.info("error provider Jwt " + e.getMessage());
            throw e;
        }

    }

    public UserDto getUserDto(String token) throws JsonProcessingException {
        try {
            UserDto userDto = jwtService.getUserDto(token);
            return userDto;
        } catch (Exception e) {
            throw e;
        }

    }

    public String refresToken(String token) throws JsonProcessingException {
        try {
            UserDto userDto = jwtService.getUserDto(token);

            if (deleteToken(token).equals(" sesion cerrada")) {

                return createToken(userDto);
            }
            return "";

        } catch (Exception e) {
            // TODO: handle exception
            throw e;
        }

    }

    // validar token si existe en la lista blanca
    public String validatetokenInlistToken(String jwt) {

        if (listToken.containsKey(jwt)) {
            return null;
        }
        
        return "sesion cerrada";
    }

    public boolean validateIsEnableEmail(String token) {

        UserDto exists = listToken.get(token);

        if (exists == null) {
            throw new BadCredentialsException("Usuario no  tiene cuenta activa ");
        }

        return exists.isEnabled();
    }

    //
    public String deleteToken(String jwt) {

        if (!listToken.containsKey(jwt)) {

            throw new InvalidVerificationTokenException(
                    "token no existe , el usuario  ya cerro session");

        }

        listToken.remove(jwt);

        return " sesion cerrada";
    }

}
