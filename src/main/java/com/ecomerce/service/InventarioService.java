package com.ecomerce.service;

import com.ecomerce.model.Inventario;
import com.ecomerce.repository.InventarioRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Importación necesaria para @Transactional
import java.util.List;
import java.util.Optional;

/**
 * Clase de Servicio para Inventario.
 * Contiene la lógica de negocio para la gestión de productos en el inventario.
 * Interactúa con InventarioRepository para acceder a los datos.
 */
@Service // Indica que esta clase es un componente de servicio de Spring
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    // Inyección de dependencias
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * Guarda un nuevo producto en el inventario o actualiza uno existente.
     * @param inventario El objeto Inventario a guardar.
     * @return El Inventario guardado.
     */
    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    /**
     * Obtiene todos los productos del inventario.
     * @return Una lista de todos los productos.
     */
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    /**
     * Obtiene un producto del inventario por su ID.
     * @param id El ID del producto.
     * @return Un Optional que contiene el producto si se encuentra, o vacío si no.
     */
    public Optional<Inventario> obtenerPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    /**
     * Elimina un producto del inventario por su ID.
     * @param id El ID del producto a eliminar.
     */
    public void eliminar(Long id) {
        inventarioRepository.deleteById(id);
    }

    /**
     * Actualiza el stock de un producto.
     * Esta operación es transaccional para asegurar la consistencia.
     * @param id El ID del producto.
     * @param cantidad El cambio en la cantidad (positivo para añadir, negativo para quitar).
     * @return El Inventario actualizado, o Optional.empty() si el producto no existe o el stock es insuficiente.
     */
    @Transactional // Asegura que la operación de stock sea atómica
    public Optional<Inventario> actualizarStock(Long id, int cantidad) {
        return inventarioRepository.findById(id).map(producto -> {
            int nuevoStock = producto.getStock() + cantidad;
            if (nuevoStock >= 0) { // Asegura que el stock no sea negativo
                producto.setStock(nuevoStock);
                return inventarioRepository.save(producto);
            }
            return null; // Retorna null si el stock sería negativo (se manejará como Optional.empty())
        });
    }
}