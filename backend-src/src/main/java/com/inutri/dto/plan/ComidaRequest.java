package com.inutri.dto.plan;

import com.inutri.modelo.enums.TipoComida;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class ComidaRequest {

    @NotNull
    private TipoComida tipo;

    @NotNull
    private List<AlimentoPlanRequest> alimentos;

    public ComidaRequest(TipoComida tipo, List<AlimentoPlanRequest> alimentos) {
        this.tipo = tipo;
        this.alimentos = alimentos;
    }
}