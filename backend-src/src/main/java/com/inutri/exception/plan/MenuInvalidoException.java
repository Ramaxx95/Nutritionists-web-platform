package com.inutri.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MenuInvalidoException extends RuntimeException {
    public MenuInvalidoException(String message) {
        super(message);
    }
}
