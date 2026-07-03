package com.hotelreservas.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Permite mostrar los detalles sin que se repitatodo_infinitamente
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "huesped_id", nullable = false)
    private Huesped huesped;

    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @Column(nullable = false)
    private LocalDate fechaSalida;

    @Column(nullable = false)
    private Double totalPagar;

    @Column(nullable = false, length = 20)
    private String estado; // PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA

    @Column(length = 300)
    private String observaciones;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    // Este lado SÍ se muestra en el JSON
    @JsonManagedReference
    private List<DetalleReserva> detalles;
}
