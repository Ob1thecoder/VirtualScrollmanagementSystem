package com.VSS;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(        
                "http://django-service:8000",
                "http://localhost:8000",
                "http://localhost"
                )  
            .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
            .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "X-CSRFToken")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
