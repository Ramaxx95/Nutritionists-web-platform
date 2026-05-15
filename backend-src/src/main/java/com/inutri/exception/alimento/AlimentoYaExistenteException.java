package com.inutri.exception.alimento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AlimentoYaExistenteException extends RuntimeException {
    public AlimentoYaExistenteException(String mensaje) {
        super(mensaje);
    }
}