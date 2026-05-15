package com.inutri.cucumber.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.alimento.AlimentoListadoResponse;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BusquedaDeAlimentoSteps extends BaseStep {

    private String token;
    private String consultaTexto = null;
    private String consultaCategoria = null;
    private List<AlimentoListadoResponse> alimentos;
    private AlimentoListadoResponse alimentoResponse;
    private int cantidadEnEsteCaso = 0;

    @Y("^que ingrese al catálogo de alimentos$")
    public void queIngreseAlCatalogoDeAlimentos() {
        token = escenarioContext.get("token", String.class);
        respuesta = consultarCatalogoDeAlimentos(token);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Dado("que existe actualmente el alimento {string} de categoría {string}")
    public void dadoQueExisteActualmenteElAlimentoDeCategoria(String alimento, String categoria) {
        cantidadEnEsteCaso++;
    }

    @Cuando("ingreso en el buscador {string}")
    public void ingresoEnElBuscador(String consultaTexto) {
        this.consultaTexto = consultaTexto;
    }

    @Entonces("veo el alimento {string} como resultado de búsqueda")
    public void veoElAlimento(String resultado) throws Exception{
        respuesta = consultarAlimento(token, this.consultaTexto, this.consultaCategoria);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        alimentos = objectMapper.readValue(objectMapper.readTree(
                respuesta.getBody()).get("content").toString(),
                new TypeReference<List<AlimentoListadoResponse>>() {});
        assertThat(alimentos.size()).isEqualTo(cantidadEnEsteCaso);
        alimentoResponse = alimentos.removeFirst();
        assertThat(alimentoResponse.getNombre()).isEqualTo(resultado);
    }

    @Y("el alimento {string} de categoría {string}")
    public void yElAlimentoDeCategoria(String alimento, String categoria) {
        cantidadEnEsteCaso++;
    }

    @Y("el alimento {string} como resultado de búsqueda")
    public void yVeoElAlimento(String resultado) {
        alimentoResponse = alimentos.removeFirst();
        assertThat(alimentoResponse.getNombre()).isEqualTo(resultado);
    }

    @Cuando("^filtro los alimentos por la categoría \"Pescados y mariscos\"$")
    public void filtroLosAlimentosPorLaCategoria() {
        this.consultaCategoria = "Pescados y mariscos";
    }

    @Y("^filtro los alimentos por la categoría \"Carnes\"$")
    public void filtroLosAlimentosPorLaCategoriaY() {
        this.consultaCategoria = "Carnes";
        cantidadEnEsteCaso = 1;
    }

    @Y("no veo el alimento {string} como resultado de búsqueda")
    public void noVeoElAlimento(String resultado) {
        assertThat(alimentoResponse.getNombre()).isNotEqualTo(resultado);
    }
}
