package com.inutri.exception.paciente;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DatosPacienteInvalidosException extends RuntimeException {

    public DatosPacienteInvalidosException(String message) {
        super(message);
    }
}