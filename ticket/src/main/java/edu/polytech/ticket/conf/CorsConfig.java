package edu.polytech.ticket.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("api/v1/logs") // API concernée
                        .allowedOrigins("*") // Accepte toutes les origines (tu peux restreindre)
                        .allowedMethods("POST") // Autorise uniquement les requêtes POST
                        .allowedHeaders("*"); // Accepte tous les headers
            }
        };
    }
}
