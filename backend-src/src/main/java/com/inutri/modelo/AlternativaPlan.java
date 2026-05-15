package com.inutri.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alternativas_plan")
public class AlternativaPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "alternativaPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComidaPlan> comidas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlanAlimentacion plan;

    @OneToOne(mappedBy = "alternativa", cascade = CascadeType.ALL)
    private Menu menu;

    public AlternativaPlan(List<ComidaPlan> comidas, PlanAlimentacion plan){
        this.comidas = comidas;
        this.plan = plan;
    }
}
