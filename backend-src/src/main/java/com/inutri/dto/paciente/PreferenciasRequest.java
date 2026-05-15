package com.inutri.dto.paciente;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO que contiene los alimentos que un paciente prefiere o no tener en sus alternativas.
 * <p>
 * Atributos:
 * <ul>
 *     <li>
 *         <b>gustos</b>: lista de IDs de los alimentos que le gustan al paciente
 *     </li>
 *     <li>
 *         <b>desagrados</b>: lista de IDs de los alimentos que no le gustan al paciente
 *     </li>
 * </ul>
 */
@Getter
public class PreferenciasRequest {
    private List<Integer> gustos;
    private List<Integer> desagrados;

    public PreferenciasRequest() {
        this.gustos = new ArrayList<>();
        this.desagrados = new ArrayList<>();
    }

    public PreferenciasRequest(List<Integer> alimentosValidos, List<Integer> alimentosNoValidos){
        this.gustos = alimentosValidos;
        this.desagrados = alimentosNoValidos;
    }

    public void agregarAlimentoAIncluir(Integer id){
        this.gustos.add(id);
    }

    public void agregarAlimentoAExcluir(Integer id){
        this.desagrados.add(id);
    }
}
