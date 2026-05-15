package com.inutri.exception.sesion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SesionNoEncontradaException extends RuntimeException {

    public SesionNoEncontradaException(String message) {
        super(message);
    }
}