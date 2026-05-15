package com.inutri.exception.sesion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EstadoNoValidoException extends RuntimeException {

    public EstadoNoValidoException(String message) {
        super(message);
    }
}