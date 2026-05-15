package com.inutri.exception.alimento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AlimentoNoEncontradoException extends RuntimeException {

    public AlimentoNoEncontradoException(String message) {
        super(message);
    }
}