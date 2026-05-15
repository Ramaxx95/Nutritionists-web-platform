package com.inutri.dto.usuario;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UsosRestantesResponse {
    private int usosRestantes;
    private LocalDateTime ultimaActualizacionUsos;

    public UsosRestantesResponse(int usosRestantes, LocalDateTime ultimaActualizacionUsos) {
        this.usosRestantes = usosRestantes;
        this.ultimaActualizacionUsos = ultimaActualizacionUsos;
    }
}