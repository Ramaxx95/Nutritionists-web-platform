package com.inutri.modelo.enums;

public enum TipoComparacion {
    LT("<"), LTE("<="), EQ("="), GTE(">="), GT(">");

    private final String simbolo;

    TipoComparacion(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}