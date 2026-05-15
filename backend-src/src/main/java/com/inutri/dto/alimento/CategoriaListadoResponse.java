package com.inutri.dto.alimento;

public class CategoriaListadoResponse {
    private String descripcion;

    public CategoriaListadoResponse(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}