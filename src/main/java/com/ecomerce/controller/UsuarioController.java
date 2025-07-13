package com.ecomerce.controller;

import com.ecomerce.assemblers.UsuarioModelAssembler; // Importa el Assembler
import com.ecomerce.model.Usuario;
import com.ecomerce.service.UsuarioService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// Importaciones adicionales para Swagger (si las vas a usar)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de usuarios.
 * Maneja las solicitudes HTTP relacionadas con los usuarios e incorpora HATEOAS
 * para la descubribilidad de la API, utilizando un UsuarioModelAssembler.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios de la tienda ecológica") // Anotación Swagger
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler; // Inyecta el Assembler

    // Constructor con inyección de dependencias
    public UsuarioController(UsuarioService usuarioService, UsuarioModelAssembler assembler) {
        this.usuarioService = usuarioService;
        this.assembler = assembler;
    }

    /**
     * Obtiene una lista de todos los usuarios, incluyendo enlaces HATEOAS.
     * GET /api/usuarios
     * @return CollectionModel de EntityModel<Usuario> con enlaces.
     */
    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionModel.class)))
    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listar() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos().stream()
                .map(assembler::toModel) // Usa el assembler para convertir cada Usuario a EntityModel
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
    }

    /**
     * Crea un nuevo usuario, incluyendo enlaces HATEOAS en la respuesta.
     * POST /api/usuarios
     * @param usuario El objeto Usuario enviado en el cuerpo de la solicitud (JSON).
     * @return ResponseEntity con EntityModel<Usuario> del usuario creado y enlaces.
     */
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema con los datos proporcionados.")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos incompletos")
    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> crear(@RequestBody(description = "Datos del nuevo usuario a crear", required = true) Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        EntityModel<Usuario> usuarioModel = assembler.toModel(nuevoUsuario); // Usa el assembler

        return ResponseEntity.created(usuarioModel.getRequiredLink("self").toUri()).body(usuarioModel);
    }

    /**
     * Obtiene un usuario por su ID, incluyendo enlaces HATEOAS.
     * GET /api/usuarios/{id}
     * @param id El ID del usuario a buscar.
     * @return ResponseEntity con EntityModel<Usuario> si se encuentra, o notFound().
     */
    @Operation(summary = "Obtener usuario por ID", description = "Recupera los detalles de un usuario específico por su ID.")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtener(@Parameter(description = "ID del usuario a buscar", example = "1") @PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(assembler::toModel) // Usa el assembler
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un usuario por su ID.
     * DELETE /api/usuarios/{id}
     * @param id El ID del usuario a eliminar.
     * @return ResponseEntity.noContent() si se elimina con éxito, o notFound().
     */
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema por su ID.")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del usuario a eliminar", example = "1") @PathVariable Long id) {
        if (usuarioService.obtenerPorId(id).isPresent()) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
