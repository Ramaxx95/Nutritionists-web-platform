package com.inutri.dto.alimento;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AlimentoSugerenciaIAResponse extends AlimentoListadoResponse{

    private Integer cantidad;

    public AlimentoSugerenciaIAResponse(
            Integer id,
            String categoria,
            String nombre,
            Integer valorEnergetico,
            BigDecimal proteinas,
            BigDecimal carbohidratos,
            BigDecimal grasas,
            BigDecimal colesterol,
            Integer sodio,
            BigDecimal azucarAgregado,
            BigDecimal fibraAlimentaria,
            boolean noAptoCeliaco,
            boolean ultraprocesado,
            Integer cantidad) {
        super(id, categoria, nombre, valorEnergetico, proteinas, carbohidratos, grasas, colesterol, sodio, azucarAgregado, fibraAlimentaria, noAptoCeliaco, ultraprocesado);
        this.cantidad = cantidad;
    }
}
