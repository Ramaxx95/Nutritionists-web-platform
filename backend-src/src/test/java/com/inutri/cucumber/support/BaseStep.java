package com.inutri.cucumber.support;

import com.inutri.dto.plan.AlternativaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inutri.dto.paciente.ActualizacionDatosPacienteRequest;
import com.inutri.dto.paciente.RegistroPacienteRequest;
import com.inutri.dto.usuario.*;
import com.inutri.modelo.Usuario;
import com.inutri.repositorio.UsuarioRepository;


public abstract class BaseStep {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EscenarioContext escenarioContext;

    @Autowired
    private UsuarioRepository usuarioRepository;

    protected ResponseEntity<String> respuesta;

    protected ResponseEntity<String> registrarUsuario(RegistroUsuarioRequest usuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegistroUsuarioRequest> request = new HttpEntity<>(usuario, headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/usuarios", HttpMethod.POST, request, String.class);
        
        usuarioRepository.findByEmail(usuario.getEmail()).ifPresent(usuarioGuardado -> {
                usuarioGuardado.setEmailVerificado(true);
                usuarioRepository.save(usuarioGuardado);
        });

        return response;
    }

    protected ResponseEntity<String> iniciarSesion(LoginUsuarioRequest usuario) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginUsuarioRequest> request = new HttpEntity<>(usuario, headers);

        return restTemplate.exchange("/api/usuarios/login", HttpMethod.POST, request, String.class);
    }

    protected ResponseEntity<String> registrarPaciente(RegistroPacienteRequest paciente, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<RegistroPacienteRequest> request = new HttpEntity<>(paciente, headers);
        return restTemplate.exchange("/api/pacientes", HttpMethod.POST, request, String.class);
    }

    protected ResponseEntity<String> actualizarDatosPaciente(Long id, ActualizacionDatosPacienteRequest paciente, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<ActualizacionDatosPacienteRequest> request = new HttpEntity<>(paciente, headers);
        String url = "/api/pacientes/" + id;
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

    }

    protected ResponseEntity<String> obtenerPaciente(Long id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        String url = "/api/pacientes/" + id;
        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    protected ResponseEntity<String> consultarCatalogoDeAlimentos(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange("/api/alimentos", HttpMethod.GET, request, String.class);
    }

    protected ResponseEntity<String> consultarAlimento(String token, String consultaTexto, String consultaCategoria){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = "/api/alimentos?";
        if (consultaTexto != null) {
            url += "nombre=" + consultaTexto;
        }
        if (consultaTexto != null && consultaCategoria != null) {
            url += "&";
        }
        if (consultaCategoria != null) {
            url += "categoria=" + consultaCategoria;
        }

        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
    }

    protected ResponseEntity<String> guardarAlternativaPara(Long idPaciente, Long idPlan, AlternativaRequest alternativa){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(escenarioContext.get("token", String.class));

        HttpEntity<AlternativaRequest> request = new HttpEntity<>(alternativa, headers);
        String url = "/api/pacientes/" + idPaciente + "/planes/" + idPlan + "/alternativas";
        return restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
    }
}