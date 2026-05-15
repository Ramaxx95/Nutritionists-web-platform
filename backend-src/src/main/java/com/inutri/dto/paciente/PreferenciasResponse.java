package com.inutri.dto.paciente;

import com.inutri.modelo.Alimento;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PreferenciasResponse {
    private List<PreferenciaAlimento> gustos;
    private List<PreferenciaAlimento> desagrados;

    public PreferenciasResponse(){
        this.gustos = new ArrayList<>();
        this.desagrados = new ArrayList<>();
    }

    public PreferenciasResponse(List<Alimento> gustos, List<Alimento> desagrados){
        this.gustos = new ArrayList<>();
        this.desagrados = new ArrayList<>();
        for(Alimento alimento : gustos){
            this.gustos.add(new PreferenciaAlimento(alimento.getId(), alimento.getNombre(), alimento.getCategoria()));
        }
        for(Alimento alimento : desagrados){
            this.desagrados.add(new PreferenciaAlimento(alimento.getId(), alimento.getNombre(), alimento.getCategoria()));
        }
    }

    @Getter
    @Setter
    public static class PreferenciaAlimento{
        private Integer id;
        private String nombre;
        private String categoria;

        public PreferenciaAlimento(){
            this.id = null;
            this.nombre = null;
            this.categoria = null;
        }

        public PreferenciaAlimento(Integer id, String nombre, String categoria){
            this.id = id;
            this.nombre = nombre;
            this.categoria = categoria;
        }
    }
}


