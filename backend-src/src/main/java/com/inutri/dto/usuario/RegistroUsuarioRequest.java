package com.inutri.dto.usuario;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@NoArgsConstructor
public class RegistroUsuarioRequest {
    @Email(message = "Formato de email inválido.")
    @NotBlank(message = "El email no puede estar vacío.")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial."
    )
    private String contraseña;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    private String apellido;

    @NotBlank(message = "Se debe confirmar la contraseña ingresada.")
    private String confirmarContraseña;

    public RegistroUsuarioRequest(String email, String contraseña, String nombre, String apellido, String confirmarContraseña) {
        this.email = email;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
        this.confirmarContraseña = confirmarContraseña;
    }
}