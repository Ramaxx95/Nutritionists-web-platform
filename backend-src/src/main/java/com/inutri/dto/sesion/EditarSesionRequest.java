package com.inutri.dto.sesion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class EditarSesionRequest {
    
    private LocalDateTime fechaHora;
    private String notas;
    private String estado;
    @Positive(message = "El peso debe ser mayor que cero.")
    private Float peso;

    public EditarSesionRequest(LocalDateTime fechaHora, String notas, String estado, Float peso) {
        this.fechaHora = fechaHora;
        this.notas = notas;
        this.estado = estado;
        this.peso = peso;
    }
}