package com.ecomerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Clase de Modelo (Entidad) para CarritoItem.
 * Representa un producto específico dentro de un carrito de compras, junto con su cantidad.
 */
@Entity
@Table(name = "carrito_items") // Nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class CarritoItem {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID
    private Long id;

    // Relación ManyToOne con Carrito: Muchos ítems pueden pertenecer a un solo carrito.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable = false) // Columna de clave foránea en carrito_items
    private Carrito carrito;

    // Relación ManyToOne con Inventario: Muchos ítems pueden referirse al mismo producto del inventario.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false) // Columna de clave foránea en carrito_items
    private Inventario producto; // Usamos Inventario como el "producto" del carrito

    @Column(nullable = false) // Cantidad del producto en este ítem del carrito
    private Integer cantidad;

    // Puedes añadir el precio unitario en el momento de la adición para historial de precios,
    // aunque generalmente se toma del Inventario en tiempo real.
    // private Double precioUnitarioEnMomentoDeAdicion;
}