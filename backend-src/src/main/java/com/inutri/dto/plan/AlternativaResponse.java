package com.inutri.dto.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlternativaResponse {
    private Long idAlternativa;
    private List<ComidaResponse> comidas;

    public AlternativaResponse(Long id, List<ComidaResponse> comidas){
        this.idAlternativa = id;
        this.comidas = comidas;
    }
}
