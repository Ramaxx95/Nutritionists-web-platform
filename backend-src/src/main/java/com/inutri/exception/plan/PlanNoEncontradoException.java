package com.inutri.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PlanNoEncontradoException extends RuntimeException {

    public PlanNoEncontradoException(String message) {
        super(message);
    }
}