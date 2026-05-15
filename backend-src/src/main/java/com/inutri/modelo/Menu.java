package com.inutri.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "desayuno")
    private String desayuno;

    @Column(name = "almuerzo")
    private String almuerzo;

    @Column(name = "merienda")
    private String merienda;

    @Column(name = "cena")
    private String cena;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "alternativa_id", referencedColumnName = "id", nullable = false)
    private AlternativaPlan alternativa;

    public Menu (String desayuno, String almuerzo, String merienda, String cena, AlternativaPlan alternativa) {
        this.desayuno = desayuno;
        this.almuerzo = almuerzo;
        this.merienda = merienda;
        this.cena = cena;
        this.alternativa = alternativa;
    }

}
