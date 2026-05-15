package com.inutri.dto.usuario;

import com.inutri.modelo.enums.TipoSuscripcion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginUsuarioResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String token;
    private TipoSuscripcion tipoSuscripcion;
    private Integer diasRestantes;

    public LoginUsuarioResponse(Long id, String nombre, String apellido, TipoSuscripcion tipoSuscripcion, Integer diasRestantes) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoSuscripcion = tipoSuscripcion;
        this.diasRestantes = diasRestantes;
    }
}