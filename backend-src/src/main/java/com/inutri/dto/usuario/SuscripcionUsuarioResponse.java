package com.inutri.dto.usuario;

import com.inutri.modelo.enums.TipoSuscripcion;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SuscripcionUsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private TipoSuscripcion tipoSubscripcion;
    private LocalDateTime fechaUltimoPago;
    private Integer diasRestantes;
    private boolean renovacionAutomatica;

    public SuscripcionUsuarioResponse(Long id, String nombre, String apellido, TipoSuscripcion tipoSubscripcion, LocalDateTime fechaUltimoPago, Integer diasRestantes, boolean renovacionAutomatica) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoSubscripcion = tipoSubscripcion;
        this.fechaUltimoPago = fechaUltimoPago;
        this.diasRestantes = diasRestantes;
        this.renovacionAutomatica = renovacionAutomatica;
    }
}