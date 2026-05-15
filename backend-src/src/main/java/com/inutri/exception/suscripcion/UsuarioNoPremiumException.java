package com.inutri.exception.suscripcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UsuarioNoPremiumException extends RuntimeException {

    public UsuarioNoPremiumException(String message) {
        super(message);
    }
}