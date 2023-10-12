package com.alcadia.bovid.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;

import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * Clase encargada de la creacion y validacion de jwt para el inicio de sesion
 * de un Usuario
 */
@Component
public class JwtAuthenticationProvider {

    /**
     * Llave para cifrar el jwt
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

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

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hora en milisegundos

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        // String scope = customerJwt.getAuthorities().stream()
        // .map(GrantedAuthority::getAuthority)
        // .collect(Collectors.joining(" "));

        String scope = customerJwt.getRoles().stream().map(RoleDto::getAuthority).collect(Collectors.joining(" "));

        System.out.println("============Scoperoles================>" + scope);

        String tokenCreated = JWT.create()
                .withClaim("isEnabled", customerJwt.isEnabled())
                .withClaim("email", customerJwt.getEmail())
                .withClaim("roles", scope)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);

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
    public Authentication validateToken(String token) throws AuthenticationException {

        System.out.println("entre tambien aqui validateToken");
        System.out.println(token);

        // verifica el token como su firma y expiración, lanza una excepcion si algo
        // falla
        JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

        // obtener el detalle del usuario que esta en el token
        UserDto exists = listToken.get(token);

        System.out.println("============user================>" + exists);

        if (exists == null) {
            throw new BadCredentialsException("Usuario no registrado o no ha iniciado session.");
        }

        // Creo un UserDetails pero cuando voy a roles() lo que esta es una nueva
        // autoridad con prefijo ROLES_
        /*
         * UserDetails userTest =
         * User.withUsername(exists.getFullName()).password(exists.getPassword()).roles(
         * exists.getRol()).build();
         * userTest.getAuthorities().forEach(System.out::println);
         * System.out.println("imprimiendo userDetails");
         * System.out.println(userTest);
         */

        // return new UsernamePasswordAuthenticationToken(userTest, token,
        // userTest.getAuthorities());

        // return new UsernamePasswordAuthenticationToken(userTest, token,
        // Collections.singletonList(new SimpleGrantedAuthority("WRITE_PRIVILEGE")));

        HashSet<SimpleGrantedAuthority> rolesAndAuthorities = new HashSet<>();

        for (RoleDto role : exists.getRoles()) {
            rolesAndAuthorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority()));
        }

        System.out.println("=========rolesAuthenticate===========" + rolesAndAuthorities);

        // rolesAndAuthorities.add(new SimpleGrantedAuthority("ELIMINAR_PRIVILEGE")); //
        // permisos del rol

        return new UsernamePasswordAuthenticationToken(exists, token, rolesAndAuthorities);
    }

    public String deleteToken(String jwt) {

        if (!listToken.containsKey(jwt)) {
            return "No existe token";
        }

        UserDto exists = listToken.get(jwt);
        System.out.println("============user================>" + exists);
        listToken.remove(jwt);
        return exists.getEmail().toString();
    }

}
