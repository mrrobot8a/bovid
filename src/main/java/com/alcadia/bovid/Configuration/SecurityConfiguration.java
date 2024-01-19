package com.alcadia.bovid.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
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

    

        return http.build();
    }

    

   
   
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(@NonNull HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://localhost:5173/", "https://d2zpl8rr-5173.use2.devtunnels.ms"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                config.setExposedHeaders(Arrays.asList("Authorization"));
                config.setMaxAge(3600L);
                return config;
            }
        };
    }
   

}
