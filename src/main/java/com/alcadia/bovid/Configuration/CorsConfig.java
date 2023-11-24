package com.alcadia.bovid.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Configuration
public class CorsConfig implements CorsConfigurationSource{

    @Override
    @Nullable
    public CorsConfiguration getCorsConfiguration(HttpServletRequest arg0) {

        List<String> listOfOriginConfig = List.of("http://localhost:5173");
        List<String> listHttpMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(listOfOriginConfig);
        corsConfiguration.setAllowedMethods(listHttpMethods);
        // corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        
        
        // UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", corsConfiguration);

        
        return corsConfiguration;
        
    }
    
}
