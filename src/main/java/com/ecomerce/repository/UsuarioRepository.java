 package com.ecomerce.repository;

    import com.ecomerce.model.Usuario;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    /**
     * Interfaz de Repositorio para Usuario.
     * Extiende JpaRepository para proporcionar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
     * básicas y avanzadas para la entidad Usuario, sin necesidad de implementar código.
     * Spring Data JPA se encarga de la implementación en tiempo de ejecución.
     */
    @Repository // Indica que esta interfaz es un componente de repositorio de Spring.
    public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
        // JpaRepository<TipoDeEntidad, TipoDeIdDeLaEntidad>
        // Por defecto, ya tienes métodos como save(), findById(), findAll(), deleteById(), etc.

        // Puedes añadir métodos personalizados si necesitas consultas específicas que no estén cubiertas por JpaRepository.
        // Por ejemplo:
        // Optional<Usuario> findByNombreUsuario(String nombreUsuario);
        // List<Usuario> findByEmailContaining(String email);
    }
    
