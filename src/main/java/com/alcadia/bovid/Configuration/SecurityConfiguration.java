package com.alcadia.bovid.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import com.alcadia.bovid.Exception.AccessDeniedHandlerException;
import com.alcadia.bovid.Security.JwtAuthFilter;
import com.alcadia.bovid.Security.utils.Roles;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity

public class SecurityConfiguration {

    private final AccessDeniedHandlerException accessDeniedHandlerException;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // RequestMatcher authMatcher = new MvcRequestMatcher(new
        // HandlerMappingIntrospector(), "/auth/**");

        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(t -> t.accessDeniedHandler(accessDeniedHandlerException))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**", "/auth**").permitAll();
                    auth.requestMatchers("/admin/**").hasRole(Roles.ADMIN);
                    auth.requestMatchers("/user/**", "/user**").hasAnyRole(Roles.FUNCIONARIO,
                            Roles.ADMIN);
                    auth.anyRequest().authenticated();
                });

        // http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // http.oauth2ResourceServer(oauth2 -> oauth2
        // .jwt(jwt -> jwt
        // .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration
    //             .setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://d2zpl8rr-5173.use2.devtunnels.ms"));
    //     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }
   
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://localhost:5173/", "https://d2zpl8rr-5173.use2.devtunnels.ms"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        };
    }
    // @Bean
    // public JwtDecoder jwtDecoder() {
    // return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    // }

    // @Bean
    // public JwtEncoder jwtEncoder() {
    // JWK jwk = new
    // RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
    // JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    // return new NimbusJwtEncoder(jwks);
    // }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {

    // // Crear una instancia de JwtGrantedAuthoritiesConverter
    // JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new
    // JwtGrantedAuthoritiesConverter();
    // // Configurar el nombre del reclamo (claim) que contiene las autoridades en
    // //el
    // // token JWT
    // jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
    // // Configurar el prefijo para las autoridades (roles)
    // jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

    // // Crear una instancia de JwtAuthenticationConverter
    // JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    // // Configurar el JwtGrantedAuthoritiesConverter creado anteriormente como el
    // // convertidor de autoridades
    // jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    // // Retornar la instancia de JwtAuthenticationConverter configurada
    // return jwtConverter;
    // }

}
