package com.ecomerce.repository;

import com.ecomerce.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de Repositorio para Inventario.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas
 * para la entidad Inventario. Spring Data JPA se encarga de la implementación.
 */
@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // JpaRepository<TipoDeEntidad, TipoDeIdDeLaEntidad>
    // Por defecto, ya tienes métodos como save(), findById(), findAll(), deleteById(), etc.

    // Puedes añadir métodos personalizados si necesitas consultas específicas:
    // Optional<Inventario> findByNombreProducto(String nombreProducto);
    // List<Inventario> findByStockGreaterThan(Integer stock);
}
