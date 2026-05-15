package com.inutri.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "El token es obligatorio")
    private String token;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial."
    )
    private String nuevaContraseña;

    @NotBlank(message = "Confirmar contraseña es obligatorio")
    private String confirmarContraseña;

    public ResetPasswordRequest(String token, String nuevaContraseña, String confirmarContraseña) {
        this.token = token;
        this.nuevaContraseña = nuevaContraseña;
        this.confirmarContraseña = confirmarContraseña;
    }
}