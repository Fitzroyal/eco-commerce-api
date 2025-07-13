package com.ecomerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para personalizar la documentación de OpenAPI (Swagger UI).
 * Permite definir metadatos como el título, la versión y la descripción de la API.
 */
@Configuration // Indica que esta clase contiene definiciones de beans de configuración.
public class SwaggerConfig {

    /**
     * Define un bean de tipo OpenAPI para configurar la información general de la API.
     * Esta información se mostrará en la interfaz de Swagger UI.
     * @return Un objeto OpenAPI con la información personalizada.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de E-commerce Ecológico") // Título de tu API
                        .version("1.0.0") // Versión de tu API
                        .description("Documentación de la API REST para el sistema de e-commerce de productos ecológicos. " +
                                     "Incluye gestión de Usuarios, Inventario y Carritos de compra.")); // Descripción de tu API
    }
}
