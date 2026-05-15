package com.inutri.exception.laboratorio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BiomarcadorNoEncontradoException extends RuntimeException {

    public BiomarcadorNoEncontradoException(String message) {
        super(message);
    }
}