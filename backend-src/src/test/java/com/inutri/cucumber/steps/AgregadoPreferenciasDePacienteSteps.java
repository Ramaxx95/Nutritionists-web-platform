package com.inutri.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.paciente.*;
import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import io.cucumber.java.es.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AgregadoPreferenciasDePacienteSteps extends BaseStep {

    private String token;
    private PreferenciasRequest preferencias;
    @Autowired
    private AlimentoRepository alimentoRepository;
    private RegistroPacienteResponse actualizacionDatosPacienteResponse;

    // Antecedente
    @Y("estoy en la pantalla de gestión del paciente \"Alan Rivas\" sin preferencias cargadas")
    public void estoyEnLaPantallaDeGestionDePreferenciasCargadas() {
        token = escenarioContext.get("token", String.class);
        respuesta = registrarPaciente(new RegistroPacienteRequest(
                "Alan",
                "Rivas",
                null,
                null,
                null,
                "Masculino",
                null,
                175,
                80,
                "Subir de peso",
                90,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new PreferenciasRequest(new ArrayList<>(), new ArrayList<>())), token);
        preferencias = new PreferenciasRequest(new ArrayList<>(), new ArrayList<>());
    }

    // Caso 9.01
    @Dado("que no le gusta el alimento {string}")
    public void queNoLeGustaElAlimento(String alimento) {
        Optional<Alimento> alimentoEncontrado = alimentoRepository.findByNombre(alimento);
        if(alimentoEncontrado.isPresent()){
            int id = alimentoEncontrado.get().getId();
            preferencias.agregarAlimentoAExcluir(id);
        }
    }

    @Cuando("actualizo los gustos del paciente")
    public void actualizoLosGustosDelPaciente() throws Exception{
        RegistroPacienteResponse paciente = objectMapper.readValue(
                respuesta.getBody(),
                RegistroPacienteResponse.class);
        ActualizacionDatosPacienteRequest actualizacionDatosPacienteRequest = new ActualizacionDatosPacienteRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                preferencias
        );
        respuesta = actualizarDatosPaciente(paciente.getId(),actualizacionDatosPacienteRequest,token);
    }

    @Entonces("veo un mensaje de paciente actualizado")
    public void veoUnMensajeDePacienteActualizado() throws Exception{
        actualizacionDatosPacienteResponse = objectMapper.readValue(
                respuesta.getBody(),
                RegistroPacienteResponse.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Y("que le gusta el alimento {string}")
    public void queLeGustaElAlimento(String alimento) {
        Optional<Alimento> alimentoEncontrado = alimentoRepository.findByNombre(alimento);
        if(alimentoEncontrado.isPresent()){
            int id = alimentoEncontrado.get().getId();
            preferencias.agregarAlimentoAIncluir(id);
        }
    }

    @Y("veo que en sus gustos tiene {string}")
    public void queEnSusGustosTiene(String gustos) {
        assertThat(actualizacionDatosPacienteResponse.getPreferencias().getGustos().stream().map(PreferenciasResponse.PreferenciaAlimento::getId)).isEqualTo(preferencias.getGustos());
    }

    @Y("veo que en sus desagrados tiene {string}")
    public void queEnSusDesagradosTiene(String desagrados) {
        assertThat(actualizacionDatosPacienteResponse.getPreferencias().getDesagrados().stream().map(PreferenciasResponse.PreferenciaAlimento::getId)).isEqualTo(preferencias.getDesagrados());
    }

    // Caso 9.02
    @Dado("que dejo las preferencias del paciente como una lista vacia")
    public void dejoLasPreferenciasVacias(){
        preferencias = new PreferenciasRequest(Arrays.asList(6,12), List.of(5));
        respuesta = registrarPaciente(new RegistroPacienteRequest(
                "Alan",
                "Rivas",
                null,
                null,
                null,
                "Masculino",
                null,
                175,
                80,
                "Subir de peso",
                90,
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                preferencias), token);
        preferencias = new PreferenciasRequest(new ArrayList<>(), new ArrayList<>());
    }

    @Y("veo que sus gustos estan vacios")
    public void susGustosEstanVacios(){
        assertThat(actualizacionDatosPacienteResponse.getPreferencias().getGustos()).isEmpty();
    }

    @Y("veo que sus desagrados estan vacios")
    public void susDesagradosEstanVacios(){
        assertThat(actualizacionDatosPacienteResponse.getPreferencias().getDesagrados()).isEmpty();
    }

    // Caso 9.03
    @Pero("se desea cambiar sus gustos por {string} y ningun desagrado")
    public void seDeseaCambiarSusGustos(String alimento) throws Exception{
        RegistroPacienteResponse paciente = objectMapper.readValue(
                respuesta.getBody(),
                RegistroPacienteResponse.class);
        ActualizacionDatosPacienteRequest actualizacionDatosPacienteRequest = new ActualizacionDatosPacienteRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                preferencias
        );
        respuesta = actualizarDatosPaciente(paciente.getId(),actualizacionDatosPacienteRequest,token);
        Optional<Alimento> alimentoEncontrado = alimentoRepository.findByNombre(alimento);
        alimentoEncontrado.ifPresent(value -> preferencias = new PreferenciasRequest(Arrays.asList(value.getId()), new ArrayList<>()));
    }
}
