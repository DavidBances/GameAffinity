package com.gameaffinity.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "GameAffinity API", version = "1.0", description = "Documentación de la API de GameAffinity")
)
@SecurityScheme(
        name = "bearerAuth", // Nombre que identifica al esquema
        type = SecuritySchemeType.HTTP, // Tipo HTTP
        scheme = "bearer", // Token Bearer
        bearerFormat = "JWT" // Formato del Token
)
public class SwaggerConfig {
    // No requiere bean adicional; solo esto es suficiente para configurar Swagger con autenticación.
}