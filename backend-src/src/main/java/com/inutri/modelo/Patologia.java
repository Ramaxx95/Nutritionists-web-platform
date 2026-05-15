package com.inutri.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "patologias")
public class Patologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToMany
    @JoinTable(
        name = "patologias_criterios",
        joinColumns = @JoinColumn(name = "patologia_id"),
        inverseJoinColumns = @JoinColumn(name = "criterio_id")
    )
    private List<CriterioPatologia> criterios;

    public Patologia(String nombre, List<CriterioPatologia> criterios) {
        this.nombre = nombre;
        this.criterios = criterios;
    }
}