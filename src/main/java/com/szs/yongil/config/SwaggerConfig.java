package com.szs.yongil.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "SZS 과제 API 명세서",
                description = "서비스 API 명세서",
                version = "v1"))
public class SwaggerConfig {

    /**
     * Swagger JWT 기능 추가
     */
    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("auth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("auth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }
}
