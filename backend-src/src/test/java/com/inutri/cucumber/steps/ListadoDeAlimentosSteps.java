package com.inutri.cucumber.steps;

import org.springframework.http.*;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.alimento.AlimentoListadoResponse;

import io.cucumber.java.es.*;
import static org.assertj.core.api.Assertions.*;

public class ListadoDeAlimentosSteps extends BaseStep {

    @Cuando("^consulto el catalogo de alimentos$")
    public void consultoElCatalogoDeAlimentos() {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();
        
        respuesta = consultarCatalogoDeAlimentos(token);
    }

    @Entonces("^veo los alimentos existentes con su categoria, nombre, cantidad de Kcal, proteinas, carbohidratos, grasas, colesterol y sodio$")
    public void veoLosAlimentosExistentesConSuCategoriaNombreCantidadDeKcalProteinasCarbohidratosGrasasColesterolYSodio() throws Exception {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<AlimentoListadoResponse> alimentos = objectMapper.readValue(objectMapper.readTree(respuesta.getBody()).get("content").toString(), new TypeReference<List<AlimentoListadoResponse>>() {});
        assertThat(alimentos).isNotEmpty();
        assertThat(alimentos.size()).isEqualTo(13);

    }
}