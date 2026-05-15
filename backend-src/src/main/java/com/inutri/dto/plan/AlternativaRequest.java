package com.inutri.dto.plan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AlternativaRequest {
    private List<ComidaRequest> comidas;

    public AlternativaRequest(List<ComidaRequest> comidas){
        this.comidas = comidas;
    }
}
