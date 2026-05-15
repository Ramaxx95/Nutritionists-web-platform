package com.inutri.exception.suscripcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class SoloPremiumException extends RuntimeException {

    public SoloPremiumException() {
        super("Esta funcionalidad está disponible solo para usuarios con suscripción PREMIUM. Actualiza tu plan para acceder.");
    }
}