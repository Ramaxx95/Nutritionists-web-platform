package com.inutri.dto.alimento;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
public class AgregarAlimentoRequest {
    @NotBlank(message = "El nombre del alimento no puede estar vacío.")
    private String nombre;

    @NotNull(message = "Las Kcal son obligatorias.")
    @Min(value = 0, message = "Las Kcal no pueden ser negativas.")
    private Integer kcal;

    @NotNull(message = "Las proteínas son obligatorias.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Las proteínas no pueden ser negativas.")
    private BigDecimal proteinas;

    @NotNull(message = "Los carbohidratos son obligatorios.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Los carbohidratos no pueden ser negativos.")
    private BigDecimal carbohidratos;

    @NotNull(message = "Las grasas son obligatorias.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Las grasas no pueden ser negativas.")
    private BigDecimal grasas;

    public AgregarAlimentoRequest(String nombre, Integer kcal, BigDecimal proteinas, BigDecimal carbohidratos, BigDecimal grasas) {
        this.nombre = nombre;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }
}