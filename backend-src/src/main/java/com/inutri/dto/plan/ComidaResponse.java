package com.inutri.dto.plan;

import com.inutri.modelo.enums.TipoComida;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class ComidaResponse {

    private TipoComida tipo;
    private List<AlimentoPlanResponse> alimentos;

    public ComidaResponse(TipoComida tipo, List<AlimentoPlanResponse> alimentos) {
        this.tipo = tipo;
        this.alimentos = alimentos;
    }
}