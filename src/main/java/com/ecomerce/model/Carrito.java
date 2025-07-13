package com.ecomerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de Modelo (Entidad) para Carrito de Compras.
 * Representa el carrito de un usuario.
 */
@Entity
@Table(name = "carritos") // Nombre de la tabla en la base de datos
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Genera un constructor sin argumentos (requerido por JPA)
@AllArgsConstructor // Genera un constructor con todos los argumentos
public class Carrito {

    @Id // Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Estrategia de generación de ID
    private Long id;

    // Relación ManyToOne con Usuario: Un usuario puede tener un carrito.
    // FetchType.LAZY para cargar el usuario solo cuando se necesite.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true) // Columna de clave foránea, un usuario solo tiene un carrito activo
    private Usuario usuario;

    // Relación OneToMany con CarritoItem: Un carrito puede tener muchos ítems.
    // mappedBy: Indica que la relación es bidireccional y el campo 'carrito' en CarritoItem es el propietario.
    // CascadeType.ALL: Operaciones de persistencia (guardar, eliminar) en Carrito se propagan a CarritoItem.
    // orphanRemoval = true: Si un CarritoItem se desvincula de un Carrito, se elimina de la base de datos.
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

    @Column(nullable = false) // Fecha de creación del carrito
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion; // Fecha de última actualización del carrito

    // Métodos de ciclo de vida de JPA para gestionar fechas automáticamente
    @PrePersist // Se ejecuta antes de que la entidad sea persistida por primera vez
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate // Se ejecuta antes de que la entidad sea actualizada
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Método de conveniencia para añadir un ítem al carrito.
     * Asegura que la relación bidireccional se establezca correctamente.
     * @param item El CarritoItem a añadir.
     */
    public void addItem(CarritoItem item) {
        items.add(item);
        item.setCarrito(this);
    }

    /**
     * Método de conveniencia para eliminar un ítem del carrito.
     * Asegura que la relación bidireccional se rompa correctamente.
     * @param item El CarritoItem a eliminar.
     */
    public void removeItem(CarritoItem item) {
        items.remove(item);
        item.setCarrito(null); // Rompe la relación para orphanRemoval
    }
}
