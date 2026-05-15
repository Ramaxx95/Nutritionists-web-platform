package com.inutri.validacion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class FechaFuturaArgentinaValidator implements ConstraintValidator<FechaFuturaArgentina, LocalDateTime> {

    private static final ZoneId ZONA_ARGENTINA = ZoneId.of("America/Argentina/Buenos_Aires");

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        ZonedDateTime fechaArg = value.atZone(ZONA_ARGENTINA);
        ZonedDateTime ahora = ZonedDateTime.now(ZONA_ARGENTINA);
        return fechaArg.isAfter(ahora);
    }
}