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
                "http://django_service:8000"    
                 
                )  
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
