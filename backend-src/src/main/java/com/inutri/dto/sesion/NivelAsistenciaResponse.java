package com.inutri.dto.sesion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NivelAsistenciaResponse {
    private String periodo;
    private int asistencias;
    private int noAsistencias;
    private int canceladas;
    private int pendientes;

    public NivelAsistenciaResponse(String periodo, int asistencias, int noAsistencias, int canceladas, int pendientes) {
        this.periodo = periodo;
        this.asistencias = asistencias;
        this.noAsistencias = noAsistencias;
        this.canceladas = canceladas;
        this.pendientes = pendientes;
    }
}