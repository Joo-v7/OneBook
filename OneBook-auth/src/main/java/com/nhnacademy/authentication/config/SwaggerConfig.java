package com.nhnacademy.authentication.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_NAME = "Authentication API";
    private static final String API_VERSION = "1.3";
    private static final String API_DESCRIPTION = "Authentication API 명세서";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title(API_NAME)
                .description(API_DESCRIPTION)
                .version(API_VERSION);
    }
}
