package com.inutri.exception.sesion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FechaPasadaException extends RuntimeException {

    public FechaPasadaException(String message) {
        super(message);
    }
}