package com.inutri.validacion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FechaFuturaArgentinaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FechaFuturaArgentina {
    String message() default "La fecha y hora tienen que ser posteriores a la actual.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}