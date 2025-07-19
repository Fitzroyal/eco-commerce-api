E-commerce de Productos Ecológicos (Backend con Microservicios)
Este repositorio contiene el código fuente del backend para una plataforma de comercio electrónico especializada en productos ecológicos. El proyecto está diseñado bajo una arquitectura de microservicios, lo que garantiza escalabilidad, modularidad y mantenibilidad.

1. Descripción del Proyecto
El objetivo principal de este proyecto es proveer un backend robusto y eficiente para un e-commerce dedicado a productos sostenibles. Se ha implementado una arquitectura de microservicios para superar las limitaciones de los sistemas monolíticos tradicionales, permitiendo una gestión ágil de usuarios, inventario y carritos de compra.

Las funcionalidades clave incluyen:

Gestión de Usuarios: Registro, consulta y administración de perfiles de usuario.

Gestión de Inventario: Creación, actualización, consulta y eliminación de productos, incluyendo el manejo de stock.

Gestión de Carrito de Compras: Funcionalidades para añadir, actualizar, eliminar y vaciar productos del carrito de un usuario, con validación de stock en tiempo real.

2. Arquitectura del Sistema
El proyecto se estructura en tres microservicios principales, cada uno con responsabilidades bien definidas:

Microservicio de Usuarios: Encargado de toda la lógica relacionada con los usuarios.

Microservicio de Inventario: Gestiona los productos y su disponibilidad.

Microservicio de Carrito de Compras: Administra los carritos de los usuarios y la interacción con los productos.

Cada microservicio sigue una estructura de capas (Controller, Service, Repository, Model) y se comunica a través de APIs RESTful. Aunque residen en el mismo repositorio para facilitar la gestión del proyecto, están lógicamente separados, lo que permite su futura independencia en despliegue.

3. Tecnologías Utilizadas
El backend ha sido desarrollado utilizando el siguiente stack tecnológico:

Spring Boot (v3.5.3): Framework principal para el desarrollo de microservicios en Java.

Java 17: Lenguaje de programación.

MySQL: Base de datos relacional para la persistencia de datos.

Spring Data JPA: Simplifica el acceso a datos y la interacción con MySQL.

Lombok: Herramienta para reducir el código repetitivo (boilerplate).

Spring HATEOAS: Para la implementación de hipermedia en las APIs RESTful, mejorando la descubribilidad y navegabilidad.

SpringDoc OpenAPI (Swagger UI): Generación automática de documentación interactiva de la API.

JUnit 5: Framework para pruebas unitarias.

Mockito: Librería para la creación de objetos simulados (mocks) en pruebas unitarias.

JaCoCo: Herramienta de cobertura de código para análisis de pruebas.

Maven: Herramienta de construcción y gestión de dependencias del proyecto.

Postman: Utilizado para realizar pruebas de integración de los endpoints de la API.

DBeaver (o similar): Cliente de base de datos para la gestión y visualización de la información en MySQL.

4. Estructura del Proyecto
El proyecto sigue una estructura de un solo módulo Maven, donde los diferentes microservicios están organizados lógicamente dentro del paquete com.ecomerce y sus subpaquetes (controller, model, repository, service).

ecomerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ecomerce/
│   │   │       ├── EcomerceApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── CarritoController.java
│   │   │       │   ├── InventarioController.java
│   │   │       │   └── UsuarioController.java
│   │   │       ├── model/
│   │   │       │   ├── Carrito.java
│   │   │       │   ├── CarritoItem.java
│   │   │       │   ├── Inventario.java
│   │   │       │   └── Usuario.java
│   │   │       ├── repository/
│   │   │       │   ├── CarritoItemRepository.java
│   │   │       │   ├── CarritoRepository.java
│   │   │       │   ├── InventarioRepository.java
│   │   │       │   └── UsuarioRepository.java
│   │   │       └── service/
│   │   │           ├── CarritoService.java
│   │   │           ├── InventarioService.java
│   │   │           └── UsuarioService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-test.properties
│   └── test/
│       └── java/
│           └── com/ecomerce/
│               ├── EcomerceApplicationTests.java
│               └── service/
│                   └── UsuarioServiceTest.java
├── pom.xml
└── postman/
    ├── carrito_collection.json
    ├── inventario_collection.json
    └── usuario_collection.json

5. Cómo Ejecutar el Proyecto
Para levantar y ejecutar el proyecto en tu entorno local, sigue estos pasos:

Clonar el Repositorio:

git clone <URL_DE_TU_REPOSITORIO>
cd ecomerce

Configurar la Base de Datos MySQL:

Asegúrate de tener un servidor MySQL (ej., XAMPP, Docker) instalado y en ejecución.

Verifica que los archivos src/main/resources/application-dev.properties y src/main/resources/application-test.properties contengan la configuración correcta para tu base de datos local (localhost:3306, usuario root, contraseña vacía por defecto).

# application-dev.properties (ejemplo)
spring.datasource.url=jdbc:mysql://localhost:3306/db_ecomerce_dev?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

Spring Boot creará automáticamente las bases de datos db_ecomerce_dev y db_ecomerce_test si no existen.

Compilar y Ejecutar la Aplicación:

Abre una terminal en la raíz del proyecto (ecomerce).

Limpia y compila el proyecto, y ejecuta las pruebas:

mvn clean install -U

Una vez que la compilación sea exitosa, inicia la aplicación Spring Boot:

mvn spring-boot:run

La aplicación se iniciará en http://localhost:8080.

6. Pruebas de API con Postman
Se han incluido colecciones de Postman para facilitar las pruebas de los endpoints de la API.

Importar Colecciones:

Abre Postman.

Haz clic en "Import" y selecciona "Upload Files".

Navega a la carpeta postman/ en tu repositorio clonado y selecciona los archivos usuario_collection.json, inventario_collection.json y carrito_collection.json.

Importa las colecciones.

Ejecutar Pruebas:

Asegúrate de que la aplicación Spring Boot esté corriendo.

En Postman, selecciona las solicitudes dentro de cada colección y envíalas.

Importante: Para las solicitudes GET, PUT y DELETE que requieren un {id} en la URL, deberás reemplazarlo con un ID real de un recurso que hayas creado previamente (ej., un usuario o un producto).

7. Documentación de API con Swagger UI
La documentación interactiva de la API está disponible a través de Swagger UI.

Acceder a Swagger UI:

Una vez que la aplicación esté en ejecución, abre tu navegador web y navega a:
http://localhost:8080/swagger-ui.html

Aquí podrás ver todos los endpoints de los microservicios, sus descripciones, parámetros y modelos de datos. También puedes probar las solicitudes directamente desde esta interfaz.

8. Desafíos Comunes y Soluciones
Durante el desarrollo, se enfrentaron desafíos típicos de proyectos Spring Boot, principalmente relacionados con la compatibilidad de versiones y la configuración:

Conflictos de Dependencias (SpringDoc OpenAPI y Spring HATEOAS): Se resolvió excluyendo explícitamente la auto-configuración de SpringDocHateoasConfiguration en la clase principal @SpringBootApplication para evitar un NoSuchMethodError debido a la incompatibilidad de versiones entre springdoc-openapi y spring-boot-starter-hateoas en Spring Boot 3.5.3.

Fallo al Determinar el Driver de la Base de Datos: Se solucionó asegurando que los archivos application-dev.properties y application-test.properties contuvieran las propiedades de conexión a MySQL correctamente definidas, ya que inicialmente estaban vacíos.

9. Próximos Pasos y Mejoras Futuras
Implementar seguridad avanzada (ej., Spring Security con JWT u OAuth2).

Desarrollar un frontend para una experiencia de usuario completa.

Añadir microservicios para la gestión de pedidos y pasarelas de pago.

Realizar pruebas unitarias más exhaustivas para los servicios de Inventario y Carrito.

Configurar y desplegar los microservicios en un entorno de nube (AWS, Azure, Google Cloud).

Implementar monitoreo y logging centralizado.