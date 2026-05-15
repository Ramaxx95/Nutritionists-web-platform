package com.inutri.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DistribucionInvalidaException extends RuntimeException {

    public DistribucionInvalidaException(String message) {
        super(message);
    }
}