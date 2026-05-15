package com.inutri.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ContraseñasNoCoincidenException extends RuntimeException {

    public ContraseñasNoCoincidenException(String message) {
        super(message);
    }
}