package com.inutri.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "alimentos")
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoria;
    private String nombre;
    @Column(name = "valor_energetico")
    private Integer valorEnergetico;
    @Column(precision = 5, scale = 2)
    private BigDecimal agua;
    @Column(precision = 5, scale = 2)
    private BigDecimal proteinas;
    @Column(name = "lipidos_totales", precision = 5, scale = 2)
    private BigDecimal grasas;
    @Column(precision = 6, scale = 2)
    private BigDecimal colesterol;
    @Column(precision = 5, scale = 3)
    private BigDecimal saturados;
    @Column(precision = 5, scale = 3)
    private BigDecimal monoinsaturados;
    @Column(precision = 5, scale = 3)
    private BigDecimal polininsaturados;
    @Column(precision = 5, scale = 3)
    private BigDecimal trans;
    @Column(name = "cis_linoleico", precision = 5, scale = 3)
    private BigDecimal cisLinoleico;
    @Column(name = "cis_alfa_linolenico", precision = 5, scale = 3)
    private BigDecimal cisAlfaLinolenico;
    @Column(precision = 5, scale = 3)
    private BigDecimal araquidonico;
    @Column(precision = 5, scale = 3)
    private BigDecimal eicosapentaenoico;
    @Column(precision = 5, scale = 3)
    private BigDecimal docosahexaenoico;
    @Column(name = "carbohidratos_disponibles", precision = 5, scale = 2)
    private BigDecimal carbohidratosDisponibles;
    @Column(name = "carbohidratos_totales", precision = 5, scale = 2)
    private BigDecimal carbohidratosTotales;
    @Column(name = "azucar_total", precision = 5, scale = 2)
    private BigDecimal azucarTotal;
    @Column(name = "azucar_agregado", precision = 5, scale = 2)
    private BigDecimal azucarAgregado;
    @Column(name = "fibra_alimentaria", precision = 5, scale = 2)
    private BigDecimal fibraAlimentaria;
    @Column(precision = 5, scale = 2)
    private BigDecimal alcohol;
    @Column(precision = 7, scale = 4)
    private BigDecimal cenizas;
    private Integer sodio;
    private Integer potasio;
    private Integer calcio;
    @Column(precision = 6, scale = 4)
    private BigDecimal cobre;
    private Integer fosforo;
    @Column(precision = 5, scale = 2)
    private BigDecimal hierro;
    private Integer magnesio;
    @Column(precision = 5, scale = 3)
    private BigDecimal zinc;
    @Column(precision = 5, scale = 3)
    private BigDecimal niacina;
    @Column(name = "folato_efd")
    private Integer folatoEFD;
    @Column(name = "acido_folico")
    private Integer acidoFolico;
    @Column(name = "vitamina_a")
    private Integer vitaminaA;
    private Integer retinol;
    @Column(precision = 5, scale = 3)
    private BigDecimal tiamina;
    @Column(precision = 5, scale = 3)
    private BigDecimal riboflavina;
    @Column(name = "vitamina_b12", precision = 5, scale = 3)
    private BigDecimal vitaminaB12;
    @Column(name = "vitamina_c", precision = 6, scale = 2)
    private BigDecimal vitaminaC;
    @Column(name = "vitamina_d", precision = 5, scale = 2)
    private BigDecimal vitaminaD;

    @Column(name = "no_apto_celiaco", nullable = false)
    private boolean noAptoCeliaco;

    @Column(nullable = false)
    private boolean ultraprocesado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Alimento(String categoria, String nombre, Integer valorEnergetico, BigDecimal proteinas, BigDecimal carbohidratos, BigDecimal grasas, BigDecimal colesterol, Integer sodio) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.valorEnergetico = valorEnergetico;
        this.proteinas = proteinas;
        this.carbohidratosTotales = carbohidratos;
        this.grasas = grasas;
        this.colesterol = colesterol;
        this.sodio = sodio;
    }

    public BigDecimal calcularKcalAportadas(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(valorEnergetico, gramosConsumidos);
    }

    public BigDecimal calcularCarbohidratosAportados(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(carbohidratosTotales, gramosConsumidos);
    }

    public BigDecimal calcularProteinasAportadas(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(proteinas, gramosConsumidos);
    }

    public BigDecimal calcularGrasasAportadas(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(grasas, gramosConsumidos);
    }

    public BigDecimal calcularSodioAportado(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(sodio, gramosConsumidos);
    }

    public BigDecimal calcularColesterolAportado(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(colesterol, gramosConsumidos);
    }

    public BigDecimal calcularAzucarAportado(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(azucarAgregado, gramosConsumidos);
    }

    public BigDecimal calcularFibraAportada(BigDecimal gramosConsumidos) {
        return calcularPorcentaje(fibraAlimentaria, gramosConsumidos);
    }

    private BigDecimal calcularPorcentaje(BigDecimal valorPor100g, BigDecimal gramosConsumidos) {
        if (valorPor100g == null || gramosConsumidos == null) return BigDecimal.ZERO;
        return valorPor100g.multiply(gramosConsumidos)
                       .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularPorcentaje(Integer valorPor100g, BigDecimal gramosConsumidos) {
        if (valorPor100g == null || gramosConsumidos == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(valorPor100g).multiply(gramosConsumidos)
                       .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}