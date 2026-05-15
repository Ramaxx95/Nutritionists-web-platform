package com.inutri.cucumber.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.paciente.*;
import com.inutri.dto.util.PageModule;
import com.inutri.modelo.enums.ActividadFisica;
import com.inutri.servicio.PacienteService;
import io.cucumber.java.es.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListadoDePacientesSteps extends BaseStep {

    private List<RegistroPacienteResponse> pacientes;

    @Autowired
    private PacienteService pacienteService;

    @Dado("^que no tengo ningún paciente registrado$")
    public void queNoTengoNingunPacienteRegistrado() {
        String email = escenarioContext.get("email", String.class);
        Page<RegistroPacienteResponse> pacientes = pacienteService.buscarPacientesEn(PageRequest.of(0, 10), null, email);
        assertThat(pacientes.getTotalElements()).isEqualTo(0);
    }

    @Cuando("^consulto el listado de pacientes$")
    public void consultoElListadoDePacientes() {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        respuesta = restTemplate.exchange("/api/pacientes", HttpMethod.GET, request, String.class);
    }

    @Entonces("^veo que no hay ningun elemento en el mismo$")
    public void veoQueNoHayNingunElementoEnElMismo() throws Exception {
        Page<RegistroPacienteResponse> lista = objectMapper.registerModule(new PageModule()).readValue(
                respuesta.getBody(),
                new TypeReference<>() {});
        assertThat(lista.getTotalElements()).isEqualTo(0);
    }

    @Y("^veo la opción de registrar a un paciente$")
    public void veoLaOpcionDeRegistrarAUnPaciente() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Dado("^que tengo registrado al paciente \"Juan Perez\"$")
    public void queTengoRegistradoAlPacienteJuanPerez() {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        RegistroPacienteRequest paciente = new RegistroPacienteRequest(
                "Juan",
                "Perez",
                "5558536",
                "jperez@mail.com",
                ActividadFisica.Muy_leve,
                "masculino",
                LocalDate.of(1990, 5, 15),
                175,
                89,
                "Subir masa muscular",
                95,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new PreferenciasRequest(new ArrayList<>(), new ArrayList<>()));
        registrarPaciente(paciente, token);
    }

    @Y("^que tengo registrado al paciente \"Maria Lopez\"$")
    public void queTengoRegistradoAlPacienteMariaLopez() {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        RegistroPacienteRequest paciente = new RegistroPacienteRequest(
                "Maria",
                "Lopez",
                "5555387",
                "mlopez@mail.com",
                ActividadFisica.Leve,
                "femenino",
                LocalDate.of(1999, 2, 5),
                153,
                62,
                "Subir masa muscular",
                68,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new PreferenciasRequest(new ArrayList<>(), new ArrayList<>()));
        registrarPaciente(paciente, token);
    }

    @Entonces("^veo al paciente \"Maria Lopez\" en el listado$")
    public void veoAlPacienteMariaLopezEnElListado() throws Exception {
        Page<RegistroPacienteResponse> lista = objectMapper.registerModule(new PageModule()).readValue(
                respuesta.getBody(),
                new TypeReference<>() {});
        pacientes = lista.getContent();
        assertThat(pacientes.getFirst().getNombre()).isEqualTo("Maria");
        assertThat(pacientes.getFirst().getApellido()).isEqualTo("Lopez");
    }

    @Y("^veo al paciente \"Juan Perez\" en el listado$")
    public void veoAlPacienteJuanPerezEnElListado() throws Exception {
        assertThat(pacientes.getLast().getNombre()).isEqualTo("Juan");
        assertThat(pacientes.getLast().getApellido()).isEqualTo("Perez");
    }
}