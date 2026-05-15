package com.inutri.cucumber.steps;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.paciente.*;
import io.cucumber.java.es.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ActualizarPacienteSteps extends BaseStep {
    
    private String token;
    private float talla;
    private float peso;
    private RegistroPacienteResponse actualizacionDatosPacienteResponse;

    @Y ("^estoy en la pantalla de gestión del paciente \"Sofia Pedraza\" con peso \"90\" kg y talla \"150\" cm$")
    public void estoyEnLaPantalla() {
        token = escenarioContext.get("token",String.class);
        respuesta = registrarPaciente(new RegistroPacienteRequest("Sofia","Pedraza",null,null,null,"Femenino",null,150,90,"Bajar de peso",80,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new PreferenciasRequest(new ArrayList<>(), new ArrayList<>())), token);
    }

    @Dado ("^que su talla actual es de \"153\" cm$")
    public void queSuTallaActual() {
      talla = 153;
    }

    @Y ("^que su peso actual es de \"85\" kg$")
    public void queSuPeso() {
       peso = 85;
    }

    @Cuando ("^actualizo los datos de gestión$")
    public void actualizoLosDatos() throws Exception {
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
           Float.valueOf(talla),
           Float.valueOf(peso),
           null,
           null,
           null,
           null,
           null
      );
      respuesta = actualizarDatosPaciente(paciente.getId(),actualizacionDatosPacienteRequest,token);          
    }

    @Entonces ("^veo un mensaje de información actualizada$")
    public void veoUnMensaje() throws Exception{
        actualizacionDatosPacienteResponse = objectMapper.readValue(
		        respuesta.getBody(),
		        RegistroPacienteResponse.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
    } 

    @Y ("^veo que la talla ahora es de \"153\" cm$")
    public void veoQueLaTalla(){
        assertThat(actualizacionDatosPacienteResponse.getAltura()).isEqualTo(talla);

    }

    @Y ("^veo que el peso ahora es de \"85\" kg$")
    public void veoQueElPeso(){
        assertThat(actualizacionDatosPacienteResponse.getPeso()).isEqualTo(peso);

    }


    @Dado ("^que su peso actual es de \"0\" kg$")
    public void queSuPesoActual(){
       peso=0;
    }

    @Entonces ("^veo un mensaje de datos ingresados no válidos$")
    public void veoUnMensajeDeDatos(){
       assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       assertThat(respuesta.getBody()).isEqualTo("Los datos ingresados no son válidos.");
    }
}
