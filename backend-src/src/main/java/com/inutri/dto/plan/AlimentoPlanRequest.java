package com.inutri.dto.plan;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
public class AlimentoPlanRequest {

    @NotNull
    private Integer alimentoId;

    @NotNull
    private Double cantidad;

    public AlimentoPlanRequest(Integer alimentoId, Double cantidad) {
        this.alimentoId = alimentoId;
        this.cantidad = cantidad;
    }
}