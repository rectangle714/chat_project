package com.chat_project.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig{
    companion object {
        private const val SECURITY_SCHEME_NAME = "authorization"
    }

    @Bean
    fun swaggerApi(): OpenAPI = OpenAPI()
        .components(Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
            ))
        .addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEME_NAME))
        .info(
            Info()
                .title("Swagger")
                .description("BackEnd 서버 API 명세")
                .version("1.0.0")
        )
}