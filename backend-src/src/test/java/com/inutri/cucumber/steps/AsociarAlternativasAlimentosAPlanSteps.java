package com.inutri.cucumber.steps;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.plan.*;
import com.inutri.modelo.enums.EcuacionGER;
import com.inutri.modelo.enums.TipoComida;
import com.inutri.modelo.enums.TipoDistribucion;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AsociarAlternativasAlimentosAPlanSteps extends BaseStep {
    private List<PlanAlimentacionRequest> planes = new ArrayList<>();
    private PlanAlimentacionResponse planRespuesta;

    // Caso 33.01
    @Dado("que estoy armando un nuevo plan para el paciente")
    public void queEstoyArmandoUnNuevoPlan(){
        AlimentoPlanRequest galletas = new AlimentoPlanRequest(2, 100.0);
        AlimentoPlanRequest merluza = new AlimentoPlanRequest(6, 100.0);
        AlimentoPlanRequest tostada = new AlimentoPlanRequest(9, 100.0);
        AlimentoPlanRequest pollo = new AlimentoPlanRequest(10, 100.0);

        List<AlimentoPlanRequest> alimentos_desayuno = new ArrayList<>();
        alimentos_desayuno.add(galletas);
        ComidaRequest desayuno = new ComidaRequest(TipoComida.DESAYUNO, alimentos_desayuno);

        List<AlimentoPlanRequest> alimentos_almuerzo = new ArrayList<>();
        alimentos_almuerzo.add(merluza);
        ComidaRequest almuerzo = new ComidaRequest(TipoComida.ALMUERZO, alimentos_almuerzo);

        List<AlimentoPlanRequest> alimentos_merienda = new ArrayList<>();
        alimentos_merienda.add(tostada);
        ComidaRequest merienda = new ComidaRequest(TipoComida.MERIENDA, alimentos_merienda);

        List<AlimentoPlanRequest> alimentos_cena = new ArrayList<>();
        alimentos_cena.add(pollo);
        ComidaRequest cena = new ComidaRequest(TipoComida.CENA, alimentos_cena);

        List<ComidaRequest> comidas = new ArrayList<>();
        comidas.add(desayuno);
        comidas.add(almuerzo);
        comidas.add(merienda);
        comidas.add(cena);

        List<AlternativaRequest> alternativas = new ArrayList<>();
        AlternativaRequest alternativaRequest = new AlternativaRequest(comidas);
        alternativas.add(alternativaRequest);

        PlanAlimentacionRequest plan = new PlanAlimentacionRequest("Bajar de peso",
                LocalDate.now(),
                EcuacionGER.ADA,
                TipoDistribucion.TRADICIONAL,
                alternativas);
        planes.add(plan);
    }

    @Cuando("guardo el plan")
    public void guardoElPlan(){
        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<PlanAlimentacionRequest> request = new HttpEntity<>(planes.getFirst(), headers);
        Long idPaciente = escenarioContext.get("id_paciente", Long.class);
        String url = "/api/pacientes/" + idPaciente + "/planes";
        respuesta = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    @Entonces("veo que el plan fue guardado exitosamente")
    public void veoQueElPlanFueGuardado() throws Exception{
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        planRespuesta = objectMapper.readValue(
                respuesta.getBody(),
                PlanAlimentacionResponse.class);
    }

    @Y("veo que el plan posee {int} alternativa de alimentos")
    public void veoQueElPlanSoloPoseeAlternativa(int cantAlternativas){
        assertThat(planRespuesta.getAlternativas().size()).isEqualTo(cantAlternativas);
    }

    // Caso 33.02
    @Dado("que el paciente ya tiene un plan armado")
    public void elPacienteYaTieneUnPlanArmado() throws Exception{

        queEstoyArmandoUnNuevoPlan();
        guardoElPlan();

        planRespuesta = objectMapper.readValue(
                respuesta.getBody(),
                PlanAlimentacionResponse.class);
    }

    @Cuando("armo una nueva alternativa de alimentos")
    public void armoUnaNuevaAlternativa(){
        AlimentoPlanRequest manzana = new AlimentoPlanRequest(1, 100.0);
        AlimentoPlanRequest hamburguesa = new AlimentoPlanRequest(13, 100.0);
        AlimentoPlanRequest galleta = new AlimentoPlanRequest(2, 100.0);
        AlimentoPlanRequest arroz = new AlimentoPlanRequest(12, 100.0);

        List<ComidaRequest> comidas = new ArrayList<>();

        List<AlimentoPlanRequest> alDesayuno = new ArrayList<>();
        alDesayuno.add(manzana);
        ComidaRequest desayuno = new ComidaRequest(TipoComida.DESAYUNO, alDesayuno);

        List<AlimentoPlanRequest> alAlmuerzo = new ArrayList<>();
        alAlmuerzo.add(hamburguesa);
        ComidaRequest almuerzo = new ComidaRequest(TipoComida.ALMUERZO, alAlmuerzo);

        List<AlimentoPlanRequest> alMerienda = new ArrayList<>();
        alMerienda.add(galleta);
        ComidaRequest merienda = new ComidaRequest(TipoComida.MERIENDA, alMerienda);

        List<AlimentoPlanRequest> alCena = new ArrayList<>();
        alCena.add(arroz);
        ComidaRequest cena = new ComidaRequest(TipoComida.CENA, alCena);

        comidas.add(desayuno);
        comidas.add(almuerzo);
        comidas.add(merienda);
        comidas.add(cena);

        AlternativaRequest alternativa = new AlternativaRequest(comidas);
        respuesta = guardarAlternativaPara(escenarioContext.get("id_paciente", Long.class), planRespuesta.getId(), alternativa);
    }

    @Entonces("veo un mensaje de alternativa guardada")
    public void veoUnMensajeDeAlternativaGuardada() throws Exception{
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
        planRespuesta = objectMapper.readValue(
                respuesta.getBody(),
                PlanAlimentacionResponse.class);
    }

}
