package com.inutri.cucumber.steps;

import io.cucumber.java.es.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import com.inutri.cucumber.support.BaseStep;
import com.inutri.dto.plan.*;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.*;
import com.inutri.repositorio.AlimentoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ArmadoDePlanSteps extends BaseStep {

    private String url;
    private EcuacionGER ecuacion;
    private TipoDistribucion distribucion;
    private List<AlimentoPlanRequest> alimentosDesayuno;
    private List<AlimentoPlanRequest> alimentosAlmuerzo;
    private List<AlimentoPlanRequest> alimentosMerienda;
    private List<AlimentoPlanRequest> alimentosCena;
    private PlanAlimentacionResponse planCreado;

    @Autowired
    private AlimentoRepository alimentoRepository;
    
    @Dado("^que estoy en la pantalla de armado de plan para el paciente$")
    public void queEstoyEnLaPantallaDeArmadoDePlanParaElPaciente() {
        Long idPaciente = escenarioContext.get("id_paciente", Long.class);
        assertThat(idPaciente).isNotNull();

        url = "/api/pacientes/" + idPaciente + "/planes";
        ecuacion = null;
        distribucion = null;
        alimentosDesayuno = null;
        alimentosAlmuerzo = null;
        alimentosMerienda = null;
        alimentosCena = null;
    }

    @Y("^que selecciono la ecuacion para VCT \"([^\"]+)\"$")
    public void queSeleccionoLaEcuacionParaVCT(String ecuacionStr) {
        ecuacion = EcuacionGER.valueOf(ecuacionStr);;
    }

    @Y("^que selecciono la distribucion de macronutrientes \"([^\"]+)\"$")
    public void queSeleccionoLaDistribucionDeMacronutrientes(String distribucionStr) {
        distribucion = TipoDistribucion.valueOf(distribucionStr);
    }

    @Y("^agrego el alimento \"([^\"]+)\" en la comida \"([^\"]+)\" con cantidad \"(\\d+)\" gramos$")
    public void agregoElAlimentoEnLaComidaConCantidad(String nombreAlimento, String tipo, int cantidad) {
        Alimento alimentoEncontrado = alimentoRepository.findByNombre(nombreAlimento).get();
        AlimentoPlanRequest alimento = new AlimentoPlanRequest(alimentoEncontrado.getId(), (double) cantidad);
        switch (TipoComida.valueOf(tipo)) {
            case DESAYUNO -> {
                if(alimentosDesayuno == null) {
                    alimentosDesayuno = new ArrayList<>();
                }
                alimentosDesayuno.add(alimento);
            }
            case ALMUERZO -> {
                if(alimentosAlmuerzo == null) {
                    alimentosAlmuerzo = new ArrayList<>();
                }
                alimentosAlmuerzo.add(alimento);
            }
            case MERIENDA -> {
                if(alimentosMerienda == null) {
                    alimentosMerienda = new ArrayList<>();
                }
                alimentosMerienda.add(alimento);
            }
            case CENA -> {
                if(alimentosCena == null) {
                    alimentosCena = new ArrayList<>();
                }
                alimentosCena.add(alimento);
            }
        }
    }

    @Y("^selecciono las 4 comidas$")
    public void seleccionoLas4Comidas() {
        AlimentoPlanRequest alimentoDesayuno = new AlimentoPlanRequest(alimentoRepository.findByNombre("Leche descremada fluida, con 50% más de proteínas").get().getId(), 100.00);
        if(alimentosDesayuno == null) {
            alimentosDesayuno = new ArrayList<>();
        }
        alimentosDesayuno.add(alimentoDesayuno);
        AlimentoPlanRequest alimentoAlmuerzo = new AlimentoPlanRequest(alimentoRepository.findByNombre("Pollo con piel (horno/parrilla)").get().getId(), 100.00);
        if(alimentosAlmuerzo == null) {
            alimentosAlmuerzo = new ArrayList<>();
        }
        alimentosAlmuerzo.add(alimentoAlmuerzo);
        AlimentoPlanRequest alimentoMerienda = new AlimentoPlanRequest(alimentoRepository.findByNombre("Tostadas light").get().getId(), 100.00);
        if(alimentosMerienda == null) {
            alimentosMerienda = new ArrayList<>();
        }
        alimentosMerienda.add(alimentoMerienda);
        AlimentoPlanRequest alimentoCena = new AlimentoPlanRequest(alimentoRepository.findByNombre("Hamburguesa de carne vacuna (carnicería)").get().getId(), 100.00);
        if(alimentosCena == null) {
            alimentosCena = new ArrayList<>();
        }
        alimentosCena.add(alimentoCena);
    }

    @Cuando("^toco el boton de guardar el plan$")
    public void tocoElBotonDeGuardarElPlan() {
        List<ComidaRequest> comidas = new ArrayList<>();
        List<AlternativaRequest> alternativas = new ArrayList<>();

        if(alimentosDesayuno != null) {
            comidas.add(new ComidaRequest(TipoComida.DESAYUNO,alimentosDesayuno));
        }
        if(alimentosAlmuerzo != null) {
            comidas.add(new ComidaRequest(TipoComida.ALMUERZO,alimentosAlmuerzo));
        }
        if(alimentosMerienda != null) {
            comidas.add(new ComidaRequest(TipoComida.MERIENDA,alimentosMerienda));
        }
        if(alimentosCena != null) {
            comidas.add(new ComidaRequest(TipoComida.CENA,alimentosCena));
        }
        AlternativaRequest alternativaRequest = new AlternativaRequest(comidas);
        alternativas.add(alternativaRequest);

        PlanAlimentacionRequest plan = new PlanAlimentacionRequest("Bajar de peso.", LocalDate.now(), (ecuacion != null) ? ecuacion : EcuacionGER.ADA, (distribucion != null) ? distribucion : TipoDistribucion.TRADICIONAL, alternativas);

        String token = escenarioContext.get("token", String.class);
        assertThat(token).isNotNull();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<PlanAlimentacionRequest> request = new HttpEntity<>(plan, headers);
        respuesta = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    @Entonces("^veo un mensaje de plan guardado exitosamente$")
    public void veoUnMensajeDePlanGuardadoExitosamente() throws Exception {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        planCreado = objectMapper.readValue(
                respuesta.getBody(),
                PlanAlimentacionResponse.class);
    }

    @Entonces("^veo un mensaje de comida faltante$")
    public void veoUnMensajeDeComidaFaltante() {
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).isEqualTo("Faltan las siguientes comidas: [ALMUERZO]");
    }

    @Y("^la cantidad de Kcal recomendadas es \"([0-9]+\\.?[0-9]*)\" Kcal por dia$")
    public void laCantidadDeKcalRecomendadasEs(String kcalString) {
        BigDecimal kcal = new BigDecimal(kcalString);
        assertThat(planCreado.getKcalObjetivo().setScale(2, RoundingMode.HALF_UP)).isEqualTo(kcal);
    }

    @Y("^la cantidad de carbohidratos recomendadas es \"([0-9]+\\.?[0-9]*)\" g por dia$")
    public void laCantidadDeCarbohidratosRecomendadasEs(String carbohidratosString) {
        BigDecimal carbohidratosRecomendados = new BigDecimal(carbohidratosString);
        assertThat(planCreado.getCarbohidratosObjetivo().setScale(2, RoundingMode.HALF_UP)).isEqualTo(carbohidratosRecomendados);
    }

    @Y("^la cantidad de proteinas recomendadas es \"([0-9]+\\.?[0-9]*)\" g por dia$")
    public void laCantidadDeProteinasRecomendadasEs(String proteinasString) {
        BigDecimal proteinasRecomendadas = new BigDecimal(proteinasString);
        assertThat(planCreado.getProteinasObjetivo().setScale(2, RoundingMode.HALF_UP)).isEqualTo(proteinasRecomendadas);
    }

    @Y("^la cantidad de grasas recomendadas es \"([0-9]+\\.?[0-9]*)\" g por dia$")
    public void laCantidadDeGrasasRecomendadasEs(String grasasString) {
        BigDecimal grasasRecomendadas = new BigDecimal(grasasString);
        assertThat(planCreado.getGrasasObjetivo().setScale(2, RoundingMode.HALF_UP)).isEqualTo(grasasRecomendadas);
    }
}