package com.alcadia.bovid.Security;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.alcadia.bovid.Exception.InvalidTokenException;
import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

    /**
     * Llave para cifrar el jwt
     */
    @Value("${jwt.secret.key}")
    private String secretKey;

    /**
     * Crea un nuevo jwt en base al cliente recibido por parametro y lo agrega a la
     * lista blanca
     * 
     * @param customerJwt Cliente a utilizar en la creacion del jwt
     * @return Jwt creado
     */
    public String createToken(UserDto customerJwt) {

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000 * 2); // 1 hora en milisegundos

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
                .withClaim("fullname", customerJwt.getFirstName()+" "+customerJwt.getLastName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);

        return tokenCreated;
    }

    public String getClaimEmail(String token) {
        return JWT.decode(token).getClaim("email").asString();
    }

    public boolean validateToken(String token) throws JsonProcessingException {
        // si se dispara una excepción, significa que el token no es válido
        if (isTokenExpired(token) != null)
            return true;

        if (!getUserDto(token).isEnabled())
            return true;

        return false;

    }

    public UserDto getUserDto(String token) throws JsonProcessingException {

        String email = JWT.decode(token).getClaim("email").asString();
        String fullName = JWT.decode(token).getClaim("fullname").asString();
        String rolesString = JWT.decode(token).getClaim("roles").asString();
        boolean isEnabled = JWT.decode(token).getClaim("isEnabled").asBoolean();

        List<RoleDto> roles = Arrays.stream(rolesString.split(" "))
                .map(roleAuthority -> new RoleDto(roleAuthority, isEnabled))
                .collect(Collectors.toList());

        return UserDto.builder()
                .fullname(fullName)
                .email(email)
                .roles(roles)
                .enabled(isEnabled)
                .build();
    }

    public String validateFirma(String token) throws InvalidTokenException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt = JWT.decode(token);
            algorithm.verify(jwt);

            // Si no se lanza una excepción, significa que la firma es válida
            return null;
        } catch (JWTVerificationException e) {
            return "Firma del token inválida";
        }
    }

    public String isTokenExpired(String token) {

        Date now = new Date();

        if (extractExpiration(token).before(now)) {
            Date expirationDate = extractExpiration(token);
            long timeUntilExpiration = expirationDate.getTime() - now.getTime();

            long hours = TimeUnit.MILLISECONDS.toHours(timeUntilExpiration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeUntilExpiration) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeUntilExpiration) % 60;

            log.info(expirationDate.toString());

            return String.format("El token expiro en %d horas, %d minutos y %d segundos a las %s.", hours, minutes,
                    seconds,
                    expirationDate.toString());
        }

        return null;
    }

    private Date extractExpiration(String token) {

        System.out.println("Issu" + JWT.decode(token).getExpiresAt());

        return JWT.decode(token).getExpiresAt();
    }

}
