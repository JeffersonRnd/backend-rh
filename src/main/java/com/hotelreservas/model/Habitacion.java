package com.hotelreservas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "habitacion")
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String numero; // Ej: 101, 202

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_habitacion_id", nullable = false)
    private TipoHabitacion tipoHabitacion;

    @Column(nullable = false)
    private Double precioPorNoche;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(length = 500)
    private String servicios; // Wifi, TV, Jacuzzi, etc.

    @Column(nullable = false)
    private Boolean disponible = true;
}