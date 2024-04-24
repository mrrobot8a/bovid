package com.alcadia.bovid.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.alcadia.bovid.Security.JwtAuthFilter;
import com.alcadia.bovid.Security.JwtAuthenticationProvider;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase de configuración para la creación de Beans a utilizar
 * //
 */
// @RequiredArgsConstructor
@RequiredArgsConstructor
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.alcadia.bovid.Component")
@Configuration
public class ApplicationConfig {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * Bean de Password Encoder para inyeccion
     * 
     * @return Implemetación BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Primary
    // @Bean
    // public RequestMappingHandlerMapping requestMappingHandlerMapping() {
    //     return new RequestMappingHandlerMapping();
    // }

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new ObjectMapper();
    }

    /**
     * Bean de JwtAuthFilter para inyeccion
     * 
     * @return Implementación JwtAuthFilter
     */
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtAuthenticationProvider);
    }

    

}
