package com.alcadia.bovid.Security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alcadia.bovid.Models.Dto.RoleDto;
import com.alcadia.bovid.Models.Dto.UserDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

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
        Date validity = new Date(now.getTime() + 3600000*2); // 1 hora en milisegundos

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

        return tokenCreated;
    }

    public String  getClaimEmail(String token){
        return JWT.decode(token).getClaim("email").asString();
    }

    public boolean validateToken(String token) {
        return (validatefirma(token).isEmpty() && !isTokenExpired(token).isEmpty());

    }

    public String validatefirma(String token) {

        String firma = JWT.decode(token).getSignature();
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        if (firma == null || firma.isEmpty() || firma.isBlank() || firma != algorithm.toString()) {
            return null;
        }

        return "La firma no es valida : Token no valido";
    }

    private Date extractExpiration(String token) {

      System.out.println("Issu"+JWT.decode(token).getExpiresAt());
        
        return JWT.decode(token).getExpiresAt();
    }

    public String isTokenExpired(String token) {

        Date now = new Date();

        if (extractExpiration(token).before(now)) {
            Date expirationDate = extractExpiration(token);
            long timeUntilExpiration = expirationDate.getTime() - now.getTime();

            long hours = TimeUnit.MILLISECONDS.toHours(timeUntilExpiration);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeUntilExpiration) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeUntilExpiration) % 60;


            log.info(expirationDate.toString() );

            return String.format("El token expiro en %d horas, %d minutos y %d segundos a las %s.", hours, minutes,
                    seconds,
                    expirationDate.toString());
        }

        return null;
    }

}
