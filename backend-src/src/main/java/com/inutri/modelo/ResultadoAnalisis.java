package com.inutri.modelo;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "resultados_analisis")
public class ResultadoAnalisis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analisis_id", nullable = false)
    private AnalisisLaboratorio analisis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biomarcador_id", nullable = false)
    private Biomarcador biomarcador;
}