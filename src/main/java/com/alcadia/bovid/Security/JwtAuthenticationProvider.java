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


/**
 * Clase encargada de la creacion y validacion de jwt para el inicio de sesion
 * de un Usuario
 */
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
     */
    public String createToken(UserDto customerJwt) {

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
         
            return null;

            // throw new CustomerNotExistException("Token no valido:" + e.getMessage());
        }

    }

    //
    public String deleteToken(String jwt) {

        if (!listToken.containsKey(jwt)) {

            throw new InvalidVerificationTokenException(
                    "token no existe , el usuario  ya cerro session o no ha iniciado ", null, HttpStatus.BAD_REQUEST);

        }

        listToken.remove(jwt);

        return " sesion cerrada";
    }

    public String validatetokenInlistToken(String jwt) {

        if (!listToken.containsKey(jwt)) {
            return "sesion cerrada";
        }
        return null;

    }

}
