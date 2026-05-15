package com.inutri.modelo.enums;

public enum ActividadFisica {
    Muy_leve("Muy leve"),
    Leve("Leve"),
    Moderada("Moderada"),
    Intensa("Intensa");

    private final String descripcion;

    ActividadFisica(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}