    package com.ecomerce.model;

    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.AllArgsConstructor;

    import java.time.LocalDate;

    /**
     * Clase de Modelo (Entidad) para Usuario.
     * Representa la tabla 'usuarios' en la base de datos.
     * Utiliza Lombok para generar automáticamente getters, setters, constructores, etc.
     * Se ha recreado el archivo para asegurar que no esté vacío o corrupto.
     */
    @Entity // Indica que esta clase es una entidad JPA y se mapea a una tabla de base de datos.
    @Table(name = "usuarios") // Especifica el nombre de la tabla en la base de datos.
    @Data // Anotación de Lombok para generar getters, setters, toString, equals y hashCode.
    @NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos. REQUERIDO POR JPA.
    @AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos.
    public class Usuario {

        @Id // Marca el campo como la clave primaria de la entidad.
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la estrategia de generación de ID (autoincremental en MySQL).
        private Long id;

        @Column(nullable = false) // El nombre es obligatorio
        private String nombre;

        @Column(nullable = false) // El apellido es obligatorio
        private String apellido;

        @Column(nullable = false, unique = true) // El email es obligatorio y debe ser único
        private String email;

        @Column(nullable = false) // La contraseña es obligatoria (¡recuerda hashearla!)
        private String password; // Cambiado a 'password' por convención, pero 'contraseña' también es válido

        private String telefono; // Puede ser nulo
        private String direccion; // Puede ser nulo

        @Column(nullable = false) // La fecha de registro es obligatoria
        private LocalDate fechaRegistro;

        private LocalDate fechaNacimiento; // Puede ser nulo
        private String genero; // Puede ser nulo

        // Método para establecer la fecha de registro automáticamente al crear un usuario
        @PrePersist
        protected void onCreate() {
            if (fechaRegistro == null) { // Solo si no se ha establecido manualmente
                fechaRegistro = LocalDate.now();
            }
        }
    }
    