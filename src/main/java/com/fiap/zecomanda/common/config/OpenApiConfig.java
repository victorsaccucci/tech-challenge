package com.fiap.zecomanda.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI techChallengeOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("Tech Challenge FIAP")
                        .description("ZeComanda - Simple user management system with basic CRUD operations.")
                        .version("v0.0.1")
        );
    }
}
