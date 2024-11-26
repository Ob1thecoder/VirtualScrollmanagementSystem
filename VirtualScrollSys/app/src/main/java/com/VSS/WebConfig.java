package com.VSS;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:8000")  // Allow requests from Django
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
