package com.inutri.dto.plan;

import lombok.Getter;

@Getter
public class RegistrarMenuRequest {
    private String desayuno;
    private String almuerzo;
    private String merienda;
    private String cena;

    public RegistrarMenuRequest(String desayuno, String almuerzo, String merienda, String cena){
        this.desayuno = desayuno;
        this.almuerzo = almuerzo;
        this.merienda = merienda;
        this.cena = cena;
    }
}
