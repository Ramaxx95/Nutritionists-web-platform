package com.inutri.exception.patologia;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PatologiaNoEncontradaException extends RuntimeException {

    public PatologiaNoEncontradaException(String message) {
        super(message);
    }
}