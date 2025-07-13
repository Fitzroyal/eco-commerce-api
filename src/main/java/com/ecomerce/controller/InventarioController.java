package com.ecomerce.controller;

import com.ecomerce.assemblers.InventarioModelAssembler; // Importa el Assembler
import com.ecomerce.model.Inventario;
import com.ecomerce.service.InventarioService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
 * Controlador REST para la gestión del inventario.
 * Maneja las solicitudes HTTP relacionadas con los productos en el inventario e incorpora HATEOAS.
 */
@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "API para la gestión de productos en el inventario de la tienda ecológica")
public class InventarioController {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler; // Inyecta el Assembler

    public InventarioController(InventarioService inventarioService, InventarioModelAssembler assembler) {
        this.inventarioService = inventarioService;
        this.assembler = assembler;
    }

    /**
     * Obtiene una lista de todos los productos en el inventario, incluyendo enlaces HATEOAS.
     * GET /api/inventario
     * @return CollectionModel de EntityModel<Inventario> con enlaces.
     */
    @Operation(summary = "Obtener todos los productos del inventario", description = "Recupera una lista de todos los productos disponibles en el inventario.")
    @ApiResponse(responseCode = "200", description = "Lista de productos recuperada exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionModel.class)))
    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listar() {
        List<EntityModel<Inventario>> inventarios = inventarioService.obtenerTodos().stream()
                .map(assembler::toModel) // Usa el assembler
                .collect(Collectors.toList());

        return CollectionModel.of(inventarios, linkTo(methodOn(InventarioController.class).listar()).withSelfRel());
    }

    /**
     * Crea un nuevo producto en el inventario, incluyendo enlaces HATEOAS.
     * POST /api/inventario
     * @param inventario El objeto Inventario enviado en el cuerpo de la solicitud (JSON).
     * @return ResponseEntity con EntityModel<Inventario> del producto creado y enlaces.
     */
    @Operation(summary = "Crear un nuevo producto en el inventario", description = "Registra un nuevo producto con su descripción, precio y stock inicial.")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventario.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos incompletos")
    @PostMapping
    public ResponseEntity<EntityModel<Inventario>> crear(@RequestBody(description = "Datos del nuevo producto a crear", required = true) Inventario inventario) {
        Inventario nuevoInventario = inventarioService.guardar(inventario);
        EntityModel<Inventario> inventarioModel = assembler.toModel(nuevoInventario); // Usa el assembler

        return ResponseEntity.created(inventarioModel.getRequiredLink("self").toUri()).body(inventarioModel);
    }

    /**
     * Obtiene un producto del inventario por su ID, incluyendo enlaces HATEOAS.
     * GET /api/inventario/{id}
     * @param id El ID del producto a buscar.
     * @return ResponseEntity con EntityModel<Inventario> si se encuentra, o notFound().
     */
    @Operation(summary = "Obtener producto por ID", description = "Recupera los detalles de un producto específico del inventario por su ID.")
    @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventario.class)))
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtener(@Parameter(description = "ID del producto a buscar", example = "1") @PathVariable Long id) {
        return inventarioService.obtenerPorId(id)
                .map(assembler::toModel) // Usa el assembler
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto del inventario por su ID.
     * DELETE /api/inventario/{id}
     * @param id El ID del producto a eliminar.
     * @return ResponseEntity.noContent() si se elimina con éxito, o notFound().
     */
    @Operation(summary = "Eliminar un producto del inventario", description = "Elimina un producto del inventario por su ID.")
    @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del producto a eliminar", example = "1") @PathVariable Long id) {
        if (inventarioService.obtenerPorId(id).isPresent()) {
            inventarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Actualiza el stock de un producto existente, incluyendo enlaces HATEOAS.
     * PUT /api/inventario/{id}/stock?cantidad={cantidad}
     * @param id El ID del producto.
     * @param cantidad La cantidad a añadir (positivo) o quitar (negativo) del stock.
     * @return ResponseEntity con el Inventario actualizado, o un error si no se encuentra o el stock es insuficiente.
     */
    @Operation(summary = "Actualizar stock de un producto", description = "Ajusta la cantidad de stock de un producto existente. Puede ser positivo (añadir) o negativo (quitar).")
    @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventario.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida, stock insuficiente o producto no encontrado")
    @PutMapping("/{id}/stock")
    public ResponseEntity<EntityModel<Inventario>> actualizarStock(@Parameter(description = "ID del producto", example = "1") @PathVariable Long id,
                                                                   @Parameter(description = "Cantidad a ajustar (ej. 10 para añadir, -5 para quitar)", example = "5") @RequestParam int cantidad) {
        return inventarioService.actualizarStock(id, cantidad)
                .map(assembler::toModel) // Usa el assembler
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
