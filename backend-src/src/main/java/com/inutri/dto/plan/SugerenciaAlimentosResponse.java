package com.inutri.dto.plan;
import lombok.Getter;
import java.util.List;

/**
 * DTO que contiene los alimentos sugeridos para todas las alternativas y los tokens gastados
 * que resultaron de dichas sugerencias.
 * <p>
 * Atributos:
 * <ul>
 *     <li>
 *         <b>comida</b>: lista de 4 items 'SugerenciaAlimentosEnComidaResponse'
 *     </li>
 *     <li>
 *         <b>tokensUsados</b>: cantidad de tokens que se necesitaron usar para obtener los alimentos
 *     </li>
 * </ul>
 */
@Getter
public class SugerenciaAlimentosResponse {

    private final List<SugerenciaAlimentosEnComidaResponse> comida;
    private final Integer tokensUsados;

    public SugerenciaAlimentosResponse(List<SugerenciaAlimentosEnComidaResponse> comida, Integer tokensUsados) {
        this.comida = comida;
        this.tokensUsados = tokensUsados;
    }
}
