package com.ecomerce.repository;

import com.ecomerce.model.Carrito;
import com.ecomerce.model.Usuario; // Necesario para el método findByUsuario
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Interfaz de Repositorio para Carrito.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas
 * para la entidad Carrito.
 */
@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    // Métodos por defecto: save(), findById(), findAll(), deleteById(), etc.

    /**
     * Busca un carrito por el usuario al que pertenece.
     * Útil para encontrar el carrito activo de un usuario.
     * @param usuario El objeto Usuario.
     * @return Un Optional que contiene el carrito si se encuentra, o vacío si no.
     */
    Optional<Carrito> findByUsuario(Usuario usuario);

    // Puedes añadir métodos personalizados para buscar carritos por estado, etc.
    // Optional<Carrito> findByUsuarioAndEstado(Usuario usuario, String estado);
}
