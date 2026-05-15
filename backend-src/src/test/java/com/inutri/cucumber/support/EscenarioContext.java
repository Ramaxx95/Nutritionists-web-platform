package com.inutri.cucumber.support;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EscenarioContext {

    private final Map<String, Object> datos = new HashMap<>();

    public void set(String clave, Object valor) {
        datos.put(clave, valor);
    }

    public <T> T get(String clave, Class<T> tipo) {
        return tipo.cast(datos.get(clave));
    }

    public void limpiar() {
        datos.clear();
    }
}