package com.inutri.dto.usuario;

import lombok.*;

@Getter
@NoArgsConstructor
public class LoginUsuarioRequest {
    private String email;
    private String contraseña;

    public LoginUsuarioRequest(String email, String contraseña) {
        this.email = email;
        this.contraseña = contraseña;
    }
}