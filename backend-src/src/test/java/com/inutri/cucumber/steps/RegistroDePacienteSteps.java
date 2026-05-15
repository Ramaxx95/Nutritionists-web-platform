package com.inutri.cucumber.steps;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.laboratorio.AnalisisLaboratorioRequest;
import com.inutri.dto.paciente.*;
import com.inutri.modelo.enums.ActividadFisica;

import io.cucumber.java.es.*;

import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistroDePacienteSteps extends BaseStep {

    private String nombre;
    private String apellido;

    private String telefono;  
    private String mail;
    private ActividadFisica actividadFisica;

    private String sexo;
    private LocalDate fechaNacimiento;
    private float altura;
    private float peso;
    private String objetivo;
    private float pesoObjetivo;
    private List<Integer> patologias;
    private List<String> alergenos;
    private List<AnalisisLaboratorioRequest> analisis;
    private PreferenciasRequest preferencias;

    @Y("^que tengo registrado al paciente de nombre \"([^\"]+)\" y apellido \"([^\"]+)\" de genero \"([^\"]+)\" con peso \"(\\d+)\" kg, talla \"(\\d+)\" cm y fecha de nacimiento \"([^\"]+)\"$")
    public void queTengoRegistradoAlPacienteDeNombreYApellidoGeneroConPesoTallaYAños(String unNombre, String unApellido, String genero, int unPeso, int talla, String unaFecha) throws Exception {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        RegistroPacienteRequest paciente = new RegistroPacienteRequest(
                unNombre,
                unApellido,
                "1160948512",
                "unMail@hotmail.com",
                ActividadFisica.Moderada,
                genero,
                LocalDate.parse(unaFecha),
                talla,
                unPeso,
                "Bajar de peso.",
                80,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new PreferenciasRequest(new ArrayList<>(), new ArrayList<>()));
        respuesta = registrarPaciente(paciente, token);

        escenarioContext.set("id_paciente", objectMapper.readValue(respuesta.getBody(),RegistroPacienteResponse.class).getId());
    }

    @Dado("^una persona con nombre \"([^\"]+)\" y apellido \"([^\"]+)\", de sexo \"([^\"]+)\", fecha de nacimiento \"([^\"]+)\", talla \"(\\d+)\" " +
            "cm, peso \"(\\d+)\" kg, objetivo \"([^\"]+)\", peso objetivo \"(\\d+)\" " +
            "y ninguna condición de salud$")
    public void unaPersonaConNombreSexoEdadTallaPesoYObjetivo(String unNombre, String unApellido, String genero, String unaFecha, int talla, int unPeso, String unObjetivo, int unPesoObjetivo) {
        nombre = unNombre;
        apellido = unApellido;
        telefono = "45112233";
        mail = "OTROmAIL@gmail.com";
        actividadFisica = ActividadFisica.Muy_leve;
        sexo = genero;
        fechaNacimiento = LocalDate.parse(unaFecha);
        altura = talla;
        peso = unPeso;
        objetivo = unObjetivo;
        pesoObjetivo = unPesoObjetivo;
        patologias = new ArrayList<>();
        alergenos = new ArrayList<>();
        analisis = new ArrayList<>();
        preferencias = new PreferenciasRequest(new ArrayList<>(), new ArrayList<>());
    }

    @Cuando("^lo registro como nuevo paciente$")
    public void loRegistroComoNuevoPaciente() {
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        RegistroPacienteRequest paciente = new RegistroPacienteRequest(
                nombre,
                apellido,
                telefono,
                mail,
                actividadFisica,
                sexo,
                fechaNacimiento,
                altura,
                peso,
                objetivo,
                pesoObjetivo,
                patologias,
                alergenos,
                analisis,
                preferencias);
        respuesta = registrarPaciente(paciente, token);

    }

    @Entonces("^veo un mensaje de paciente registrado$")
    public void veoUnMensajeDePacienteRegistrado() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Y("^veo la pantalla de gestión del paciente$")
    public void veoLaPantallaDeGestionDelPaciente() throws Exception {
        RegistroPacienteResponse pacienteCreado = objectMapper.readValue(
                respuesta.getBody(),
                RegistroPacienteResponse.class);
        assertThat(pacienteCreado).isNotNull();
        assertThat(pacienteCreado.getNombre()).isEqualTo(nombre);
        assertThat(pacienteCreado.getApellido()).isEqualTo(apellido);
        assertThat(pacienteCreado.getTelefono()).isEqualTo(telefono);
        assertThat(pacienteCreado.getMail()).isEqualTo(mail);
        assertThat(pacienteCreado.getActividadFisica()).isEqualTo(actividadFisica);
        assertThat(pacienteCreado.getSexo()).isEqualTo(sexo);
        assertThat(pacienteCreado.getFechaNacimiento()).isEqualTo(fechaNacimiento);
        assertThat(pacienteCreado.getAltura()).isEqualTo(altura);
        assertThat(pacienteCreado.getPeso()).isEqualTo(peso);
        assertThat(pacienteCreado.getPatologias().isEmpty()).isTrue();
        assertThat(pacienteCreado.getPreferencias().getGustos().isEmpty()).isTrue();
        assertThat(pacienteCreado.getPreferencias().getDesagrados().isEmpty()).isTrue();
    }

    @Entonces("^veo un mensaje de campos obligatorios no informados$")
    public void veoUnMensajeDeCamposObligatoriosNoInformados() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).contains("El nombre no puede estar vacío.");
        assertThat(respuesta.getBody()).contains("El apellido no puede estar vacío.");
        assertThat(respuesta.getBody()).contains("El objetivo no puede estar vacío.");
    }
}