package com.mliaedu.benchmark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        
        // Exposer les IDs dans les réponses JSON
        config.exposeIdsFor(
            com.mliaedu.benchmark.entity.Category.class,
            com.mliaedu.benchmark.entity.Item.class
        );

        // Configuration CORS si nécessaire
        cors.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*");
    }
}

