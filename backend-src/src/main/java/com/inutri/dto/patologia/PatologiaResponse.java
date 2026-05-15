package com.inutri.dto.patologia;

import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
public class PatologiaResponse {
    private Integer id;
    private String nombre;
    private List<CriterioResponse> criterios;

    public PatologiaResponse (Integer id, String nombre, List<CriterioResponse> criterios) {
        this.id = id;
        this.nombre = nombre;
        this.criterios = criterios;
    }
}