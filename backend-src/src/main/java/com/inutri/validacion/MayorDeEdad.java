package com.inutri.validacion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MayorDeEdadValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MayorDeEdad {
    String message() default "El paciente debe ser mayor de edad.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}