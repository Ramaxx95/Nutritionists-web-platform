package com.inutri.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ComidasFaltantesException extends RuntimeException {

    public ComidasFaltantesException(String message) {
        super(message);
    }
}