package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Absolute path mapping so images serve live instantly from resources/static/images
        String imageDir = Paths.get("src/main/resources/static/images/")
                .toAbsolutePath().normalize().toUri().toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/", imageDir);
    }

    // CORS is now configured once, in CorsConfig, instead of here too.
}