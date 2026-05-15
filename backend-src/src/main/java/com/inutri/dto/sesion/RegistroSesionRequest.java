package com.inutri.dto.sesion;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.inutri.validacion.FechaFuturaArgentina;

@Getter
@Setter
@NoArgsConstructor
public class RegistroSesionRequest {
    @NotNull
    @FechaFuturaArgentina
    private LocalDateTime fechaHora;

    private String notas;

    public RegistroSesionRequest(LocalDateTime fechaHora, String notas) {
        this.fechaHora = fechaHora;
        this.notas = notas;
    }
}