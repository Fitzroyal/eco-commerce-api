package com.ecomerce.assemblers;

import com.ecomerce.controller.CarritoController;
import com.ecomerce.model.Carrito;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Un Assembler para la entidad Carrito.
 * Transforma un objeto Carrito en un EntityModel<Carrito> añadiéndole enlaces HATEOAS.
 */
@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<Carrito, EntityModel<Carrito>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Carrito> toModel(Carrito carrito) {
        return EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerOcrearCarrito(carrito.getUsuario().getId())).withSelfRel(), // Enlace a sí mismo (GET por usuarioId)
                linkTo(methodOn(CarritoController.class).agregarProductoAlCarrito(carrito.getUsuario().getId(), null)).withRel("agregarItem"),
                linkTo(methodOn(CarritoController.class).vaciarCarrito(carrito.getUsuario().getId())).withRel("vaciarCarrito")
        );
    }
}
