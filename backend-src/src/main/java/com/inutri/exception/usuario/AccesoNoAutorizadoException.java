package com.inutri.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class AccesoNoAutorizadoException extends RuntimeException {

    public AccesoNoAutorizadoException(String message) {
        super(message);
    }
}