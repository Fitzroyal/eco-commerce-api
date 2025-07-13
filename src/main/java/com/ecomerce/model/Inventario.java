package com.ecomerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clase de Modelo (Entidad) para Inventario (o Producto).
 * Representa los ítems disponibles en la tienda.
 */
@Entity
@Table(name = "inventario") // Nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Inventario {

    @Id // Marca el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID (autoincremental)
    private Long id;

    @Column(nullable = false, unique = true) // Nombre del producto: no nulo y único
    private String nombreProducto;

    @Column(nullable = false) // Descripción del producto: no nula
    private String descripcion;

    @Column(nullable = false) // Precio del producto: no nulo
    private Double precio;

    @Column(nullable = false) // Stock disponible: no nulo
    private Integer stock;

    // Puedes añadir más campos según las necesidades de tu e-commerce ecológico:
    // private String imageUrl; // URL de la imagen del producto
    // private String categoria; // Categoría del producto (ej. "Hogar", "Cuidado Personal")
    // private Boolean esOrganico; // Si el producto es orgánico
    // private String certificacion; // Tipo de certificación ecológica
}
