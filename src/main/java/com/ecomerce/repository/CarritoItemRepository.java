package com.ecomerce.repository;

import com.ecomerce.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de Repositorio para CarritoItem.
 * Extiende JpaRepository para proporcionar operaciones CRUD básicas
 * para la entidad CarritoItem.
 */
@Repository // Indica que esta interfaz es un componente de repositorio de Spring
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    // Métodos por defecto: save(), findById(), findAll(), deleteById(), etc.

    // Puedes añadir métodos personalizados si necesitas buscar ítems por carrito o producto.
    // List<CarritoItem> findByCarritoId(Long carritoId);
    // Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
