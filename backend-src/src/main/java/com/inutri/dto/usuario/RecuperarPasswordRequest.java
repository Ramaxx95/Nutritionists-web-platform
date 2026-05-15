package com.inutri.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecuperarPasswordRequest {

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El email ingresado no es válido.")
    private String email;

    public RecuperarPasswordRequest(String email) {
        this.email = email;
    }
}