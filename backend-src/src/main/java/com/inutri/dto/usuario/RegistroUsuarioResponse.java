package com.inutri.dto.usuario;

import lombok.*;

@Getter
@NoArgsConstructor
public class RegistroUsuarioResponse {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;

    public RegistroUsuarioResponse(Long id, String email, String nombre, String apellido) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
    }
}