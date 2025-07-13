package com.ecomerce.assemblers;

import com.ecomerce.controller.UsuarioController;
import com.ecomerce.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Un Assembler para la entidad Usuario.
 * Transforma un objeto Usuario en un EntityModel<Usuario> añadiéndole enlaces HATEOAS.
 */
@Component // Indica que esta clase es un componente de Spring y puede ser inyectada.
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    /**
     * Convierte un objeto Usuario en su representación EntityModel con enlaces HATEOAS.
     * @param usuario El objeto Usuario a transformar.
     * @return Un EntityModel<Usuario> con enlaces.
     */
    @SuppressWarnings("null") // Suprime advertencias de nulidad para linkTo/methodOn
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtener(usuario.getId())).withSelfRel(), // Enlace a sí mismo (GET por ID)
                linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"), // Enlace a la colección completa
                linkTo(methodOn(UsuarioController.class).eliminar(usuario.getId())).withRel("eliminar") // Enlace para eliminar
                // Puedes añadir más enlaces aquí, como para actualizar (PUT)
        );
    }
}