package com.inutri.modelo;

import com.inutri.modelo.enums.EcuacionGER;
import com.inutri.modelo.enums.TipoDistribucion;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planes")
public class PlanAlimentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private EcuacionGER ecuacion;

    @Enumerated(EnumType.STRING)
    private TipoDistribucion distribucion;

    @Column(name = "kcal_objetivo", precision = 7, scale = 2)
    private BigDecimal kcalObjetivo;

    @Column(name = "carbohidratos_objetivo", precision = 7, scale = 2)
    private BigDecimal carbohidratosObjetivo;

    @Column(name = "proteinas_objetivo", precision = 7, scale = 2)
    private BigDecimal proteinasObjetivo;

    @Column(name = "grasas_objetivo", precision = 7, scale = 2)
    private BigDecimal grasasObjetivo;

    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Paciente paciente;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlternativaPlan> alternativas;

    public PlanAlimentacion(String objetivo, LocalDate fechaInicio, List<AlternativaPlan> alternativas) {
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.alternativas = alternativas;
    }

    public void agregarAlternativa(AlternativaPlan alternativa){
        this.alternativas.add(alternativa);
    }

    public Menu obtenerMenuDe(Long alternativaId){
        Optional<AlternativaPlan> alternativa = this.alternativas
                .stream()
                .filter(alternativaPlan -> alternativaPlan.getId().equals(alternativaId))
                .findFirst();
        Menu menu = null;
        if(alternativa.isPresent()){
            menu = alternativa.get().getMenu();
        }
        return menu;
    }

    public void agregarMenuA(Menu menu, Long alternativaId){
        for(AlternativaPlan alternativa : this.alternativas){
            if(alternativa.getId().equals(alternativaId)){
                alternativa.setMenu(menu);
                break;
            }
        }
    }

    public boolean contieneAlternativa(Long alternativaId){
        for(AlternativaPlan alternativa : this.alternativas){
            if(alternativa.getId().equals(alternativaId)){
                return true;
            }
        }
        return false;
    }

    public AlternativaPlan getAlternativa(Long alternativaId){
        for(AlternativaPlan alternativa : this.alternativas){
            if(alternativa.getId().equals(alternativaId)){
                return alternativa;
            }
        }
        return null;
    }

}