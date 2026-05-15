package com.inutri.dto.plan;

import com.inutri.dto.alimento.AlimentoListadoResponse;
import com.inutri.dto.alimento.AlimentoSugerenciaIAResponse;
import com.inutri.modelo.enums.TipoComida;
import lombok.Getter;

import java.util.List;

/**
 * DTO que contiene el tipo de comida y los alimentos que fueron sugeridos
 * para esa comida.
 * <p>
 * Atributos:
 * <ul>
 *     <li>
 *         <b>tipoComida</b>: especifica si los alimentos son para DESAYUNO | ALMUERZO | MERIENDA | CENA
 *     </li>
 *     <li>
 *         <b>alimentos</b>: contiene la lista de alimentos a usar en 'tipoComida'
 *     </li>
 * </ul>
 */
@Getter
public class SugerenciaAlimentosEnComidaResponse {

    private final TipoComida tipoComida;
    private final List<AlimentoSugerenciaIAResponse> alimentos;

    public SugerenciaAlimentosEnComidaResponse(TipoComida tipoComida, List<AlimentoSugerenciaIAResponse> alimentos){
        this.tipoComida = tipoComida;
        this.alimentos = alimentos;
    }
}
