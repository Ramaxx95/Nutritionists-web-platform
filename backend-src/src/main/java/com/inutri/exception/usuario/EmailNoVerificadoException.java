package com.inutri.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class EmailNoVerificadoException extends RuntimeException {

    public EmailNoVerificadoException(String message) {
        super(message);
    }
}