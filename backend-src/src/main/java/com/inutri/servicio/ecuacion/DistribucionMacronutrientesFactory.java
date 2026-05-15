package com.inutri.servicio.ecuacion;

import com.inutri.modelo.DistribucionMacronutrientes;
import com.inutri.modelo.enums.TipoDistribucion;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DistribucionMacronutrientesFactory {
    public static DistribucionMacronutrientes crearPorTipo(TipoDistribucion tipo) {
        switch (tipo) {
            case PROTEICA:
                return new DistribucionMacronutrientes(BigDecimal.valueOf(50), BigDecimal.valueOf(20), BigDecimal.valueOf(30));
            case TRADICIONAL:
            default:
                return new DistribucionMacronutrientes(BigDecimal.valueOf(55), BigDecimal.valueOf(15), BigDecimal.valueOf(30));
        }
    }
}