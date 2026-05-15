package com.inutri.dto.alimento;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor
public class AlimentoListadoResponse {

    protected Integer id;
    protected String categoria;
    protected String nombre;
    protected Integer valorEnergetico;
    protected BigDecimal proteinas;
    protected BigDecimal carbohidratos;
    protected BigDecimal grasas;
    protected BigDecimal colesterol;
    protected Integer sodio;
    protected BigDecimal azucarAgregado;
    protected BigDecimal fibraAlimentaria;
    protected boolean noAptoCeliaco;
    protected boolean ultraprocesado;

    public AlimentoListadoResponse(Integer id, String categoria, String nombre, Integer valorEnergetico, BigDecimal proteinas, BigDecimal carbohidratos, BigDecimal grasas, BigDecimal colesterol, Integer sodio, BigDecimal azucarAgregado, BigDecimal fibraAlimentaria, boolean noAptoCeliaco, boolean ultraprocesado) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.valorEnergetico = valorEnergetico;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.colesterol = colesterol;
        this.sodio = sodio;
        this.azucarAgregado = azucarAgregado;
        this.fibraAlimentaria = fibraAlimentaria;
        this.noAptoCeliaco = noAptoCeliaco;
        this.ultraprocesado = ultraprocesado;
    }
}