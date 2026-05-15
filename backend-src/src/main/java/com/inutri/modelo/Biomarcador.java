package com.inutri.modelo;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "biomarcadores")
public class Biomarcador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    private String unidad;

    private String categoria;

    @OneToMany(mappedBy = "biomarcador")
    private List<RangoReferencia> rangosReferencia;
}