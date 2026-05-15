package com.inutri.exception.plan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AlternativaNoEncontradaException extends RuntimeException{
    public AlternativaNoEncontradaException(String message){super(message);}
}
