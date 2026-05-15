package com.inutri.dto.alimento;

import java.math.BigDecimal;
import lombok.*;

@Getter
@NoArgsConstructor
public class AlimentoDetalleResponse {

    protected Integer id;
    protected String categoria;
    protected String nombre;
    protected Integer valorEnergetico;
    protected BigDecimal agua;
    protected BigDecimal proteinas;
    protected BigDecimal grasas;
    protected BigDecimal colesterol;
    protected BigDecimal saturados;
    protected BigDecimal monoinsaturados;
    protected BigDecimal polininsaturados;
    protected BigDecimal trans;
    protected BigDecimal cisLinoleico;
    protected BigDecimal cisAlfaLinolenico;
    protected BigDecimal araquidonico;
    protected BigDecimal eicosapentaenoico;
    protected BigDecimal docosahexaenoico;
    protected BigDecimal carbohidratosDisponibles;
    protected BigDecimal carbohidratosTotales;
    protected BigDecimal azucarTotal;
    protected BigDecimal azucarAgregado;
    protected BigDecimal fibraAlimentaria;
    protected BigDecimal alcohol;
    protected BigDecimal cenizas;
    protected Integer sodio;
    protected Integer potasio;
    protected Integer calcio;
    protected BigDecimal cobre;
    protected Integer fosforo;
    protected BigDecimal hierro;
    protected Integer magnesio;
    protected BigDecimal zinc;
    protected BigDecimal niacina;
    protected Integer folatoEFD;
    protected Integer acidoFolico;
    protected Integer vitaminaA;
    protected Integer retinol;
    protected BigDecimal tiamina;
    protected BigDecimal riboflavina;
    protected BigDecimal vitaminaB12;
    protected BigDecimal vitaminaC;
    protected BigDecimal vitaminaD;

    public AlimentoDetalleResponse(Integer id, String categoria, String nombre, Integer valorEnergetico, BigDecimal agua, BigDecimal proteinas, BigDecimal grasas, BigDecimal colesterol, BigDecimal saturados, BigDecimal monoinsaturados, BigDecimal polininsaturados, BigDecimal trans, BigDecimal cisLinoleico, BigDecimal cisAlfaLinolenico, BigDecimal araquidonico, BigDecimal eicosapentaenoico, BigDecimal docosahexaenoico, BigDecimal carbohidratosDisponibles, BigDecimal carbohidratosTotales, BigDecimal azucarTotal, BigDecimal azucarAgregado, BigDecimal fibraAlimentaria, BigDecimal alcohol, BigDecimal cenizas, Integer sodio, Integer potasio, Integer calcio, BigDecimal cobre, Integer fosforo, BigDecimal hierro, Integer magnesio, BigDecimal zinc, BigDecimal niacina, Integer folatoEFD, Integer acidoFolico, Integer vitaminaA, Integer retinol, BigDecimal tiamina, BigDecimal riboflavina, BigDecimal vitaminaB12, BigDecimal vitaminaC, BigDecimal vitaminaD) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.valorEnergetico = valorEnergetico;
        this.agua = agua;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.colesterol = colesterol;
        this.saturados = saturados;
        this.monoinsaturados = monoinsaturados;
        this.polininsaturados = polininsaturados;
        this.trans = trans;
        this.cisLinoleico = cisLinoleico;
        this.cisAlfaLinolenico = cisAlfaLinolenico;
        this.araquidonico = araquidonico;
        this.eicosapentaenoico = eicosapentaenoico;
        this.docosahexaenoico = docosahexaenoico;
        this.carbohidratosDisponibles = carbohidratosDisponibles;
        this.carbohidratosTotales = carbohidratosTotales;
        this.azucarTotal = azucarTotal;
        this.azucarAgregado = azucarAgregado;
        this.fibraAlimentaria = fibraAlimentaria;
        this.alcohol = alcohol;
        this.cenizas = cenizas;
        this.sodio = sodio;
        this.potasio = potasio;
        this.calcio = calcio;
        this.cobre = cobre;
        this.fosforo = fosforo;
        this.hierro = hierro;
        this.magnesio = magnesio;
        this.zinc = zinc;
        this.niacina = niacina;
        this.folatoEFD = folatoEFD;
        this.acidoFolico = acidoFolico;
        this.vitaminaA = vitaminaA;
        this.retinol = retinol;
        this.tiamina = tiamina;
        this.riboflavina = riboflavina;
        this.vitaminaB12 = vitaminaB12;
        this.vitaminaC = vitaminaC;
        this.vitaminaD = vitaminaD;
    }
}