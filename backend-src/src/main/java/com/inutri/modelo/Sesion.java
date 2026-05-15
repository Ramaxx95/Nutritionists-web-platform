package com.inutri.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.inutri.modelo.enums.EstadoSesion;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSesion estado;

    public Sesion(Paciente paciente, LocalDateTime fechaHora, String notas, EstadoSesion estado) {
        this.paciente = paciente;
        this.fechaHora = fechaHora;
        this.notas = notas;
        this.estado = estado;
    }
}