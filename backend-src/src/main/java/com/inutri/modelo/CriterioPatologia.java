package com.inutri.modelo;

import com.inutri.modelo.enums.TipoComparacion;
import com.inutri.modelo.enums.TipoCriterio;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "criterios")
public class CriterioPatologia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_criterio", nullable = false)
    private TipoCriterio tipoCriterio;
    
    private BigDecimal limite;
    
    @Enumerated(EnumType.STRING)
    private TipoComparacion comparacion;
    private String unidad;
    private String nutriente;

    @Column(name = "flag_objetivo")
    private String flagObjetivo;

    @ElementCollection
    @CollectionTable(
        name = "criterios_categorias",
        joinColumns = @JoinColumn(name = "criterio_id")
    )
    @Column(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    private boolean critico;

    public CriterioPatologia(String descripcion, TipoCriterio tipoCriterio, BigDecimal limite, TipoComparacion comparacion, String unidad, String nutriente, String flagObjetivo, List<String> categorias, boolean critico) {
        this.descripcion = descripcion;
        this.tipoCriterio = tipoCriterio;
        this.limite = limite;
        this.comparacion = comparacion;
        this.unidad = unidad;
        this.nutriente = nutriente;
        this.flagObjetivo = flagObjetivo;
        this.categorias = categorias;
        this.critico = critico;
    }
}