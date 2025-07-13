package com.ecomerce.controller;

import com.ecomerce.assemblers.CarritoModelAssembler; // Importa el Assembler
import com.ecomerce.model.Carrito;
import com.ecomerce.model.CarritoItem;
import com.ecomerce.service.CarritoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Importaciones adicionales para Swagger (si las vas a usar)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador REST para la gestión del carrito de compras.
 * Maneja las solicitudes HTTP relacionadas con los carritos y sus ítems, e incorpora HATEOAS.
 */
@RestController
@RequestMapping("/api/carritos")
@Tag(name = "Carritos", description = "API para la gestión de carritos de compra de los usuarios")
public class CarritoController {

    private final CarritoService carritoService;
    private final CarritoModelAssembler carritoAssembler; // Inyecta el Assembler

    public CarritoController(CarritoService carritoService, CarritoModelAssembler carritoAssembler) {
        this.carritoService = carritoService;
        this.carritoAssembler = carritoAssembler;
    }

    /**
     * Obtiene el carrito de un usuario específico. Si no existe, lo crea.
     * Incluye enlaces HATEOAS.
     * GET /api/carritos/{usuarioId}
     * @param usuarioId El ID del usuario.
     * @return ResponseEntity con EntityModel<Carrito> del usuario.
     */
    @Operation(summary = "Obtener o crear carrito de usuario", description = "Recupera el carrito de un usuario específico. Si el usuario no tiene un carrito, se crea uno nuevo.")
    @ApiResponse(responseCode = "200", description = "Carrito recuperado o creado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Carrito.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{usuarioId}")
    public ResponseEntity<EntityModel<Carrito>> obtenerOcrearCarrito(@Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        try {
            Carrito carrito = carritoService.obtenerOcrearCarrito(usuarioId);
            EntityModel<Carrito> carritoModel = carritoAssembler.toModel(carrito); // Usa el assembler
            return ResponseEntity.ok(carritoModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usuario no encontrado
        }
    }

    /**
     * Agrega un producto al carrito de un usuario.
     * Incluye enlaces HATEOAS en la respuesta del CarritoItem.
     * POST /api/carritos/{usuarioId}/items
     * Cuerpo de la solicitud: { "productoId": 1, "cantidad": 2 }
     * @param usuarioId El ID del usuario.
     * @param request Un objeto que contenga productoId y cantidad.
     * @return ResponseEntity con EntityModel<CarritoItem> agregado/actualizado.
     */
    @Operation(summary = "Agregar producto al carrito", description = "Añade un producto al carrito de un usuario. Si el producto ya existe en el carrito, se actualiza su cantidad.")
    @ApiResponse(responseCode = "200", description = "Producto agregado/actualizado en el carrito",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarritoItem.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, stock insuficiente o cantidad inválida")
    @ApiResponse(responseCode = "404", description = "Usuario o Producto no encontrado")
    @PostMapping("/{usuarioId}/items")
    public ResponseEntity<EntityModel<CarritoItem>> agregarProductoAlCarrito(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId,
            @RequestBody(description = "ID del producto y cantidad a agregar", required = true) CarritoItemRequest request) {
        try {
            return carritoService.agregarProductoAlCarrito(usuarioId, request.getProductoId(), request.getCantidad())
                    .map(item -> EntityModel.of(item,
                            linkTo(methodOn(CarritoController.class).obtenerOcrearCarrito(usuarioId)).withRel("carrito"),
                            linkTo(methodOn(CarritoController.class).actualizarCantidadProductoEnCarrito(usuarioId, item.getProducto().getId(), item.getCantidad())).withRel("actualizarCantidad"),
                            linkTo(methodOn(CarritoController.class).eliminarProductoDelCarrito(usuarioId, item.getProducto().getId())).withRel("eliminarItem")))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Actualiza la cantidad de un producto específico en el carrito.
     * PUT /api/carritos/{usuarioId}/items/{productoId}?cantidad={nuevaCantidad}
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto en el carrito.
     * @param nuevaCantidad La nueva cantidad deseada.
     * @return ResponseEntity con EntityModel<CarritoItem> actualizado.
     */
    @Operation(summary = "Actualizar cantidad de producto en carrito", description = "Modifica la cantidad de un producto específico en el carrito del usuario.")
    @ApiResponse(responseCode = "200", description = "Cantidad de producto actualizada",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarritoItem.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, stock insuficiente o cantidad inválida")
    @ApiResponse(responseCode = "404", description = "Usuario o producto en carrito no encontrado")
    @PutMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<EntityModel<CarritoItem>> actualizarCantidadProductoEnCarrito(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId,
            @Parameter(description = "ID del producto en el carrito", example = "1") @PathVariable Long productoId,
            @Parameter(description = "Nueva cantidad deseada del producto", example = "3") @RequestParam int nuevaCantidad) {
        try {
            return carritoService.actualizarCantidadProductoEnCarrito(usuarioId, productoId, nuevaCantidad)
                    .map(item -> EntityModel.of(item,
                            linkTo(methodOn(CarritoController.class).obtenerOcrearCarrito(usuarioId)).withRel("carrito"),
                            linkTo(methodOn(CarritoController.class).actualizarCantidadProductoEnCarrito(usuarioId, item.getProducto().getId(), item.getCantidad())).withSelfRel(),
                            linkTo(methodOn(CarritoController.class).eliminarProductoDelCarrito(usuarioId, item.getProducto().getId())).withRel("eliminarItem")))
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Elimina un producto del carrito.
     * DELETE /api/carritos/{usuarioId}/items/{productoId}
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto a eliminar del carrito.
     * @return ResponseEntity.noContent() si se elimina, o ResponseEntity.notFound() si no.
     */
    @Operation(summary = "Eliminar producto del carrito", description = "Remueve un producto específico del carrito del usuario.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado del carrito exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario o producto en carrito no encontrado")
    @DeleteMapping("/{usuarioId}/items/{productoId}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId,
            @Parameter(description = "ID del producto a eliminar del carrito", example = "1") @PathVariable Long productoId) {
        try {
            boolean eliminado = carritoService.eliminarProductoDelCarrito(usuarioId, productoId);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Vacía completamente el carrito de un usuario.
     * DELETE /api/carritos/{usuarioId}/vaciar
     * @param usuarioId El ID del usuario.
     * @return ResponseEntity.noContent() si se vacía, o ResponseEntity.notFound() si el carrito no existe.
     */
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los productos del carrito de un usuario.")
    @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario o carrito no encontrado")
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        try {
            boolean vaciado = carritoService.vaciarCarrito(usuarioId);
            if (vaciado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Clase DTO (Data Transfer Object) para la solicitud de agregar/actualizar ítems al carrito.
    // Esta clase se usa para mapear el JSON de entrada de las solicitudes POST/PUT.
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CarritoItemRequest {
        private Long productoId;
        private int cantidad;
    }
}
