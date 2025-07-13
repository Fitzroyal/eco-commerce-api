package com.ecomerce.service;

import com.ecomerce.model.Carrito;
import com.ecomerce.model.CarritoItem;
import com.ecomerce.model.Inventario; // Necesario para referenciar productos
import com.ecomerce.model.Usuario; // Necesario para referenciar usuarios
import com.ecomerce.repository.CarritoRepository;
import com.ecomerce.repository.CarritoItemRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Importación necesaria para @Transactional
import java.util.Optional;

/**
 * Clase de Servicio para Carrito.
 * Contiene la lógica de negocio para la gestión de carritos de compras.
 * Interactúa con CarritoRepository, CarritoItemRepository, UsuarioService e InventarioService.
 */
@Service // Indica que esta clase es un componente de servicio de Spring
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioService usuarioService; // Inyección de UsuarioService
    private final InventarioService inventarioService; // Inyección de InventarioService

    // Constructor con inyección de dependencias
    public CarritoService(CarritoRepository carritoRepository,
                          CarritoItemRepository carritoItemRepository,
                          UsuarioService usuarioService,
                          InventarioService inventarioService) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.usuarioService = usuarioService;
        this.inventarioService = inventarioService;
    }

    /**
     * Obtiene el carrito activo de un usuario. Si no existe, crea uno nuevo.
     * @param usuarioId El ID del usuario.
     * @return El carrito del usuario.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    @Transactional // Asegura que toda la operación sea una única transacción de base de datos.
    public Carrito obtenerOcrearCarrito(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    /**
     * Agrega un producto al carrito de un usuario.
     * Si el producto ya está en el carrito, actualiza la cantidad.
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto a añadir.
     * @param cantidad La cantidad a añadir.
     * @return El CarritoItem actualizado o nuevo, o Optional.empty() si hay un problema (stock, cantidad).
     * @throws RuntimeException Si el usuario o producto no es encontrado.
     */
    @Transactional
    public Optional<CarritoItem> agregarProductoAlCarrito(Long usuarioId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            // La cantidad debe ser positiva para añadir
            return Optional.empty();
        }

        Carrito carrito = obtenerOcrearCarrito(usuarioId); // Obtiene o crea el carrito
        Inventario producto = inventarioService.obtenerPorId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // Busca si el producto ya existe en el carrito
        Optional<CarritoItem> existingItem = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        CarritoItem item;
        if (existingItem.isPresent()) {
            item = existingItem.get();
            // Calcular la cantidad total si se añade más
            int cantidadTotalDeseada = item.getCantidad() + cantidad;
            // Verificar si hay suficiente stock para la cantidad total deseada
            if (producto.getStock() < (cantidadTotalDeseada - item.getCantidad())) { // Solo verificamos el stock adicional necesario
                return Optional.empty(); // Stock insuficiente
            }
            item.setCantidad(cantidadTotalDeseada);
            inventarioService.actualizarStock(productoId, -cantidad); // Disminuir stock del inventario
        } else {
            // Verificar stock para el nuevo ítem
            if (producto.getStock() < cantidad) {
                return Optional.empty(); // Stock insuficiente
            }
            item = new CarritoItem();
            item.setProducto(producto);
            item.setCantidad(cantidad);
            carrito.addItem(item); // Añadir al carrito (establece la relación bidireccional)
            inventarioService.actualizarStock(productoId, -cantidad); // Disminuir stock del inventario
        }

        carritoRepository.save(carrito); // Guarda el carrito para persistir los cambios en los ítems
        return Optional.of(item);
    }

    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto en el carrito.
     * @param nuevaCantidad La nueva cantidad deseada.
     * @return El CarritoItem actualizado, o Optional.empty() si no se encuentra o hay error de stock/cantidad.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    @Transactional
    public Optional<CarritoItem> actualizarCantidadProductoEnCarrito(Long usuarioId, Long productoId, int nuevaCantidad) {
        if (nuevaCantidad < 0) {
            // La cantidad no puede ser negativa
            return Optional.empty();
        }

        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        Optional<CarritoItem> existingItem = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (existingItem.isPresent()) {
            CarritoItem item = existingItem.get();
            Inventario producto = item.getProducto();
            int cantidadActual = item.getCantidad();
            int diferenciaCantidad = nuevaCantidad - cantidadActual;

            if (diferenciaCantidad > 0) { // Si se está aumentando la cantidad
                if (producto.getStock() < diferenciaCantidad) {
                    return Optional.empty(); // Stock insuficiente para aumentar
                }
            }

            if (nuevaCantidad == 0) {
                // Si la nueva cantidad es 0, eliminar el ítem del carrito
                carrito.removeItem(item);
                carritoItemRepository.delete(item); // Eliminar de la base de datos
                inventarioService.actualizarStock(productoId, cantidadActual); // Devolver stock al inventario
                carritoRepository.save(carrito); // Guardar el carrito para reflejar la eliminación
                return Optional.empty(); // No hay ítem después de eliminar
            } else {
                item.setCantidad(nuevaCantidad);
                inventarioService.actualizarStock(productoId, -diferenciaCantidad); // Ajustar stock en inventario
                carritoRepository.save(carrito); // Guardar el carrito para reflejar el cambio
                return Optional.of(item);
            }
        }
        return Optional.empty(); // Ítem no encontrado en el carrito
    }

    /**
     * Elimina un producto específico del carrito.
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto a eliminar del carrito.
     * @return true si se eliminó, false si no se encontró.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    @Transactional
    public boolean eliminarProductoDelCarrito(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerOcrearCarrito(usuarioId);
        Optional<CarritoItem> existingItem = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (existingItem.isPresent()) {
            CarritoItem item = existingItem.get();
            carrito.removeItem(item); // Elimina de la lista en memoria (y por orphanRemoval de la DB)
            carritoItemRepository.delete(item); // Asegura la eliminación explícita
            inventarioService.actualizarStock(productoId, item.getCantidad()); // Devolver stock al inventario
            carritoRepository.save(carrito); // Guarda el carrito para reflejar la eliminación
            return true;
        }
        return false;
    }

    /**
     * Vacía completamente el carrito de un usuario.
     * @param usuarioId El ID del usuario.
     * @return true si se vació, false si el carrito no existe.
     * @throws RuntimeException Si el usuario no es encontrado.
     */
    @Transactional
    public boolean vaciarCarrito(Long usuarioId) {
        Usuario usuario = usuarioService.obtenerPorId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Optional<Carrito> carritoOptional = carritoRepository.findByUsuario(usuario);
        if (carritoOptional.isPresent()) {
            Carrito carrito = carritoOptional.get();
            // Devolver stock de todos los ítems al inventario antes de eliminarlos del carrito
            carrito.getItems().forEach(item -> inventarioService.actualizarStock(item.getProducto().getId(), item.getCantidad()));

            carrito.getItems().clear(); // Limpia la lista de ítems en memoria
            carritoRepository.save(carrito); // Guarda el carrito para que JPA elimine los ítems huérfanos
            return true;
        }
        return false;
    }
}
