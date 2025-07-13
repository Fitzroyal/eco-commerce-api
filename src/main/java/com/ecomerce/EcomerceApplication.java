package com.ecomerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springdoc.core.configuration.SpringDocHateoasConfiguration; // Importa la clase a excluir

/**
 * Clase principal de la aplicación Spring Boot para el e-commerce.
 * Esta clase contiene el método 'main' que arranca la aplicación.
 *
 * @SpringBootApplication es una anotación de conveniencia que agrega:
 * - @Configuration: Marca la clase como una fuente de definiciones de beans.
 * - @EnableAutoConfiguration: Habilita la configuración automática de Spring Boot.
 * - @ComponentScan: Escanea el paquete actual para componentes de Spring (como @Controller, @Service, @Repository).
 *
 * Se excluye SpringDocHateoasConfiguration para resolver un conflicto de versiones
 * con Spring Boot 3.5.3 y evitar el error NoSuchMethodError.
 */
@SpringBootApplication(exclude = { SpringDocHateoasConfiguration.class }) // ¡Línea modificada!
public class EcomerceApplication {

    /**
     * Método principal que se ejecuta al iniciar la aplicación.
     * Utiliza SpringApplication.run() para arrancar la aplicación Spring Boot.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(EcomerceApplication.class, args);
    }

}