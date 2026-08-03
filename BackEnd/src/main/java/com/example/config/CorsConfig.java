package com.example.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Single source of truth for CORS.
 *
 * This used to be split across two independent places: this class (a
 * standalone CorsFilter bean, allowing only :3000) and
 * WebConfig.addCorsMappings (allowing only :5173). Spring Security's
 * .cors(...) looks specifically for a CorsConfigurationSource bean, which
 * neither of those actually was, so which origin (if either) was really
 * in effect for a given request was ambiguous. Exposing a
 * CorsConfigurationSource bean directly here removes that ambiguity, and
 * WebConfig's addCorsMappings override has been removed to match.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Covers both common React dev servers: Create React App (3000)
        // and Vite (5173). Add your deployed frontend's origin here too
        // once this goes anywhere beyond localhost.
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:5173"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        // Allow JWT Authorization Header
        configuration.setExposedHeaders(List.of("Authorization"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}