package com.inutri.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UsosAgotadosException extends RuntimeException {

    public UsosAgotadosException(String message) {
        super(message);
    }
}