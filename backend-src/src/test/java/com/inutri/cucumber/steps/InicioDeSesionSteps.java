package com.inutri.cucumber.steps;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.usuario.LoginUsuarioRequest;
import com.inutri.dto.usuario.LoginUsuarioResponse;
import com.inutri.dto.usuario.RegistroUsuarioRequest;
import com.inutri.repositorio.EmailVerificationTokenRepository;
import com.inutri.repositorio.UsuarioRepository;

import io.cucumber.java.es.*;
import io.cucumber.java.Before;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.*;

public class InicioDeSesionSteps extends BaseStep {

    private String email;
    private String contraseña;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Before
    public void setUp() {
        escenarioContext.limpiar();
        email = null;
        contraseña = null;
        respuesta = null;
        if(usuarioRepository != null) {
            emailVerificationTokenRepository.deleteAll();
            usuarioRepository.deleteAll();
        }
    }

    @Dado("^que inicie sesion con un usuario existente$")
    public void queInicieSesionConUnUsuarioExistente() throws Exception {
        String email = "j0rgeA@hotmail.com";
        String contraseña = "c0nTr@sen1aZegurA!";
        registrarUsuario(new RegistroUsuarioRequest(email, contraseña, "Jorge", "Amador", contraseña));
        String token = objectMapper.readValue(
                iniciarSesion(new LoginUsuarioRequest(email,contraseña)).getBody(),
                LoginUsuarioResponse.class).getToken();
        
        escenarioContext.set("token", token);
        escenarioContext.set("email", email);
    }

    @Dado("^un usuario que ya existe en el sistema$")
    public void unUsuarioQueYaExisteEnElSistema() {
        RegistroUsuarioRequest unUsuario = new RegistroUsuarioRequest(
                "aleMart1nez@gmail.com",
                "contrAZ932!",
                "Alejo",
                "Martinez",
                "contrAZ932!");
        registrarUsuario(unUsuario);
        email = unUsuario.getEmail();
    }

    @Y("^su contraseña$")
    public void suContraseña() {
        contraseña = "contrAZ932!";
    }

    @Cuando("^inicio sesión con esos datos$")
    public void inicioSesionConEsos() {
        LoginUsuarioRequest unUsuario = new LoginUsuarioRequest(
                email,
                contraseña);
        respuesta = iniciarSesion(unUsuario);
    }

    @Entonces("^veo el home de la aplicación$")
    public void veoElHomeDeLaAplicacion() throws Exception {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        LoginUsuarioResponse miToken = objectMapper.readValue(
                respuesta.getBody(),
                LoginUsuarioResponse.class);
        assertThat(miToken).isNotNull();
        assertThat(miToken.getToken()).startsWith("eyJ");

    }

    @Y("^una contraseña que no es correcta$")
    public void unaContraseñaQueNoEsCorrecta() {
        contraseña = "contrZA239!";
    }

    @Entonces("^veo un mensaje de credenciales incorrectas$")
    public void veoUnMensajeDeCredencialesIncorrectas() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(respuesta.getBody()).isEqualTo("Usuario o contraseña incorrectos.");
    }

    @Dado("^un usuario que no existe en el sistema$")
    public void unUsuarioQueNoExisteEnElSistema() { 
        email = "rLozano@gmail.com";
    }

    @Y("^una contraseña$")
    public void unaContraseña() { 
        contraseña = "contrAZ932!";
    }

    @Entonces("^veo un mensaje de usuario no existente$")
    public void veoUnMensajeDeUsuarioNoExistente() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(respuesta.getBody()).isEqualTo("Usuario o contraseña incorrectos.");
    }

}
