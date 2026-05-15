package com.inutri.exception.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TokenNoValidoException extends RuntimeException {

    public TokenNoValidoException(String message) {
        super(message);
    }
}