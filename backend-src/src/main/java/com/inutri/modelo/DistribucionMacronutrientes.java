package com.inutri.modelo;

import com.inutri.exception.plan.DistribucionInvalidaException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DistribucionMacronutrientes {

    private static final BigDecimal KCAL_POR_GRAMO_CARBOHIDRATO = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_POR_GRAMO_PROTEINA = BigDecimal.valueOf(4);
    private static final BigDecimal KCAL_POR_GRAMO_GRASA = BigDecimal.valueOf(9);
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private BigDecimal porcentajeCarbohidratos;
    private BigDecimal porcentajeProteinas;
    private BigDecimal porcentajeGrasas;

    public DistribucionMacronutrientes(BigDecimal porcentajeCarbohidratos, BigDecimal porcentajeProteinas, BigDecimal porcentajeGrasas) {
        BigDecimal suma = porcentajeCarbohidratos.add(porcentajeProteinas).add(porcentajeGrasas);
        if (suma.compareTo(CIEN) != 0) {
            throw new DistribucionInvalidaException("La suma de los porcentajes de macronutrientes debe ser 100%.");
        }
        this.porcentajeCarbohidratos = porcentajeCarbohidratos;
        this.porcentajeProteinas = porcentajeProteinas;
        this.porcentajeGrasas = porcentajeGrasas;
    }

    public BigDecimal calcularGramosCarbohidratos(BigDecimal totalKcal) {
        return calcularGramosMacronutriente(totalKcal, porcentajeCarbohidratos, KCAL_POR_GRAMO_CARBOHIDRATO);
    }

    public BigDecimal calcularGramosProteinas(BigDecimal totalKcal) {
        return calcularGramosMacronutriente(totalKcal, porcentajeProteinas, KCAL_POR_GRAMO_PROTEINA);
    }

    public BigDecimal calcularGramosGrasas(BigDecimal totalKcal) {
        return calcularGramosMacronutriente(totalKcal, porcentajeGrasas, KCAL_POR_GRAMO_GRASA);
    }

    private BigDecimal calcularGramosMacronutriente(BigDecimal totalKcal, BigDecimal porcentaje, BigDecimal kcalPorGramo) {
        BigDecimal kcalMacronutriente = totalKcal
                .multiply(porcentaje)
                .divide(CIEN, 2, RoundingMode.HALF_UP);

        return kcalMacronutriente.divide(kcalPorGramo, 2, RoundingMode.HALF_UP);
    }
}