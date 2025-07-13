package com.ecomerce.assemblers;

import com.ecomerce.controller.InventarioController;
import com.ecomerce.model.Inventario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Un Assembler para la entidad Inventario.
 * Transforma un objeto Inventario en un EntityModel<Inventario> añadiéndole enlaces HATEOAS.
 */
@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        return EntityModel.of(inventario,
                linkTo(methodOn(InventarioController.class).obtener(inventario.getId())).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listar()).withRel("inventario"),
                linkTo(methodOn(InventarioController.class).eliminar(inventario.getId())).withRel("eliminar"),
                linkTo(methodOn(InventarioController.class).actualizarStock(inventario.getId(), 0)).withRel("actualizarStock")
        );
    }
}
