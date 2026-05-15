package com.inutri.exception.paciente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DatosPacienteFaltantesException extends RuntimeException {

    public DatosPacienteFaltantesException(String message) {
        super(message);
    }
}