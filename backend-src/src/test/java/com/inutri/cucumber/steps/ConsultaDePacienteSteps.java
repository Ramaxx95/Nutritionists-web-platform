package com.inutri.cucumber.steps;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.paciente.*;
import com.inutri.modelo.enums.ActividadFisica;

import io.cucumber.java.es.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsultaDePacienteSteps extends BaseStep {

    private String token;
    RegistroPacienteResponse paciente;

    @Dado("^que tengo registrado al paciente \"Gustavo Pacheco\" con peso \"(\\d+)\" kg, talla \"(\\d+)\" cm y fecha de nacimiento \"([^\"]+)\"$")
    public void queTengoRegistradoAlPaciente(int peso, int altura, String fechaNacimiento) throws Exception {
        token = escenarioContext.get("token", String.class);
        RegistroPacienteRequest paciente = new RegistroPacienteRequest(
                "Gustavo",
                "Pacheco",
                "5556358",
                "gpacheco@mail.com",
                ActividadFisica.Muy_leve,
                "masculino",
                LocalDate.parse(fechaNacimiento),
                altura,
                peso,
                "bajar de peso",
                95.0f,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new PreferenciasRequest(new ArrayList<>(), new ArrayList<>()));
        respuesta = registrarPaciente(paciente, token);

        escenarioContext.set("id_paciente", objectMapper.readValue(respuesta.getBody(),RegistroPacienteResponse.class).getId());
    }

    @Cuando("^entro a la pantalla de gestion del paciente$")
    public void entroAlPaciente() throws Exception {
        Long idPaciente = escenarioContext.get("id_paciente", Long.class);
        assertThat(idPaciente).isNotNull();

        respuesta = obtenerPaciente(idPaciente, token);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        paciente = objectMapper.readValue(respuesta.getBody(), RegistroPacienteResponse.class);
    }

    @Entonces("^veo que su nombre es \"([^\"]+)\"$")
    public void veoQueSuNombreEs(String unNombre) {
        assertThat(paciente.getNombre() + " " + paciente.getApellido()).isEqualTo(unNombre);
    }

    @Y("^veo que su peso es \"(\\d+)\" kg$")
    public void veoQueSuPesoEs(int unPeso) {
        assertThat(paciente.getPeso()).isEqualTo(unPeso);
    }

    @Y("^veo que su talla es \"(\\d+)\" cm$")
    public void veoQueSuTallaEs(int talla) {
        assertThat(paciente.getAltura()).isEqualTo(talla);
    }

    @Y("^veo que su fecha de nacimiento es \"([^\"]+)\"$")
    public void veoQueSuEdadEs(String unaFecha) {
        assertThat(paciente.getFechaNacimiento()).isEqualTo(LocalDate.parse(unaFecha));
    }
}
