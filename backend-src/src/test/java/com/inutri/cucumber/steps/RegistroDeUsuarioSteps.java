package com.inutri.cucumber.steps;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.usuario.*;
import com.inutri.repositorio.EmailVerificationTokenRepository;
import com.inutri.repositorio.UsuarioRepository;

import io.cucumber.java.es.*;
import io.cucumber.java.Before;
import static org.assertj.core.api.Assertions.*;

public class RegistroDeUsuarioSteps extends BaseStep {

    private String email;
    private String nombre;
    private String apellido;
    private String contraseña;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Before
    public void setUp() {
        email = null;
        nombre = null;
        apellido = null;
        contraseña = null;
        respuesta = null;
        if(usuarioRepository != null) {
            emailVerificationTokenRepository.deleteAll();
            usuarioRepository.deleteAll();
        }
    }

    @Dado("^un correo electronico que no esta registrado en el sistema$")
    public void unCorreoElectronicoQueNoEstaRegistradoEnElSistema() {
        email = "am0laNutrici00n@gmail.com";
    }

    @Dado("^un correo electronico que ya esta registrado en el sistema$")
    public void unCorreoElectronicoQueYaEstaRegistradoEnElSistema() {
        email = "aleMart1nez@gmail.com";
        RegistroUsuarioRequest unUsuario = new RegistroUsuarioRequest(email, "contrAZ932!", "Alejo", "Martinez", "contrAZ932!");
        registrarUsuario(unUsuario);
    }

    @Y("^un nombre y apellido$")
    public void unNombreYApellido() {
        nombre = "Daiana";
        apellido = "Minino";
    }

    @Y("^una contraseña que cumple con los requisitos de seguridad$")
    public void unaContraseñaQueCumpleConLosRequisitosDeSeguridad() {
        contraseña = "c0nTr@sen1aZegurA!";
    }

    @Y("^una contraseña que no cumple con los requisitos de seguridad$")
    public void unaContraseñaQueNoCumpleConLosRequisitosDeSeguridad() {
        contraseña = "contra";
    }
    
    @Cuando("^intento registrarme usando esos datos$")
    public void intentoRegistrarmeUsandoEsosDatos() {
        RegistroUsuarioRequest usuario = new RegistroUsuarioRequest(email, contraseña, nombre, apellido, contraseña);
        respuesta = registrarUsuario(usuario);
    }

    @Cuando("^intento registrarme poniendo una contraseña distinta en el campo de confirmacion$")
    public void intentoRegistrarmePoniendoUnaContraseñaDistintaEnElCampoDeConfirmacion() {
        RegistroUsuarioRequest usuario = new RegistroUsuarioRequest(email, contraseña, nombre, apellido, "A231@xgdsj!s3");
        respuesta = registrarUsuario(usuario);
    }

    @Entonces("^veo un mensaje de exito$")
    public void veoUnMensajeDeExito() throws Exception {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RegistroUsuarioResponse usuarioCreado = objectMapper.readValue(respuesta.getBody(), RegistroUsuarioResponse.class);
        assertThat(usuarioCreado).isNotNull();
        assertThat(usuarioCreado.getEmail()).isEqualTo(email);
        assertThat(usuarioCreado.getNombre()).isEqualTo(nombre);
        assertThat(usuarioCreado.getApellido()).isEqualTo(apellido);
    }

    @Entonces("veo un mensaje de error que me informa que ese correo ya esta en uso")
    public void veoUnMensajeDeErrorQueMeInformaQueEseCorreoYaEstaEnUso() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(respuesta.getBody()).isEqualTo("El email ya está registrado en el sistema.");
    }

    @Entonces("veo un mensaje de campos obligatorios faltantes")
    public void veoUnMensajeDeCamposObligatoriosFaltantes() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).contains("El apellido no puede estar vacío.");
        assertThat(respuesta.getBody()).contains("El nombre no puede estar vacío.");
    }

    @Entonces("veo mensajes claros de validacion que me indican que debo corregir")
    public void veoMensajesClarosDeValidacionQueMeIndicanQueDeboCorregir() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).isEqualTo("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial.");
    }

    @Entonces("veo un mensaje de que las contraseñas no coinciden")
    public void veoUnMensajeDeQueLasContraseñasNoCoinciden() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).isEqualTo("Las contraseñas ingresadas no coinciden.");
    }

}