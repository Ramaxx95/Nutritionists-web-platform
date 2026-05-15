package com.inutri.modelo;

import java.math.BigDecimal;

import com.inutri.modelo.enums.GeneroReferencia;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "rangos_referencia")
public class RangoReferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private GeneroReferencia sexo = GeneroReferencia.AMBOS;

    @Column(name = "valor_min")
    private BigDecimal valorMin;

    @Column(name = "valor_max")
    private BigDecimal valorMax;

    @ManyToOne
    @JoinColumn(name = "biomarcador_id", nullable = false)
    private Biomarcador biomarcador;
}