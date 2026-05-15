package com.inutri.dto.sesion;

import com.inutri.modelo.enums.EstadoSesion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class RegistroSesionResponse {
    private Long id;
    private Long pacienteId;
    private String nombre;
    private String apellido;
    private LocalDateTime fechaHora;
    private String notas;
    private EstadoSesion estado;
    private Float pesoPaciente;

    public RegistroSesionResponse(Long id, Long pacienteId, String nombre, String apellido, LocalDateTime fechaHora, String notas, EstadoSesion estado, Float pesoPaciente) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaHora = fechaHora;
        this.notas = notas;
        this.estado = estado;
        this.pesoPaciente = pesoPaciente;
    }
}