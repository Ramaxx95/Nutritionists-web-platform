package com.inutri.util;

import com.inutri.modelo.Paciente;
import com.inutri.modelo.Patologia;
import com.inutri.modelo.enums.TipoComida;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class PromptBuilder {

    private String prompt;

    public PromptBuilder() {
        prompt = "Quiero que me armes una lista de alimentos para crear un plan nutricional diario " +
                "de 4 comidas (desayuno, almuerzo, merienda y cena) para un paciente.\n\n";
    }

    public PromptBuilder agregarMacros(Double kcal, Double totalCarbohidratos, Double totalProteinas, Double totalGrasas) {

        prompt += "Objetivo nutricional total del dia:\n";
        prompt += "* " + kcal + " kcal\n";
        prompt += "* " + totalCarbohidratos + " gr en carbohidratos\n";
        prompt += "* " + totalProteinas + " gr en proteinas\n";
        prompt += "* " + totalGrasas + " gr en grasas\n\n";

        return this;
    }

    public PromptBuilder agregarPatologias(List<Patologia> patologias) {
        prompt += "Restricciones del paciente:\n";
        StringBuilder sb = new StringBuilder();
        for (Patologia patologia : patologias) {
            sb.append("* ").append(patologia.getNombre()).append(": ");
            patologia.getCriterios().forEach(criterio -> sb.append(criterio.getDescripcion()).append(", "));
            sb.append("\n");
        }
        prompt += sb.toString();

        return this;
    }

    public PromptBuilder agregarAlimentosRestringidos(ArrayList<String> restricciones) {
        prompt += "* Alimentos prohibidos: ";
        StringBuilder sb = new StringBuilder();
        for (String restriccion : restricciones) {
            sb.append(restriccion).append(" - ");
        }
        prompt += sb + "\n\n";

        return this;
    }

    public PromptBuilder agregarReglasDeConstruccionDePlan(){
        prompt += "Reglas de construccion del plan nutricional:\n" +
                "* El nombre del alimento en la respuesta debe ser exactamente como los que aparecen en el dataset\n" +
                "* Atributo \"Solo para desayuno y/o merienda\": si un alimento tiene un \"si\" en este atributo, eso significa que es exclusivo para usar en desayuno o merienda\n" +
                "* Atributo \"Es postre\": en caso de decir \"si\", entonces el alimento se puede usar como postre para el almuerzo o cena\n" +
                "* Atributo \"Es ultraprocesado\": cuando el valor en este atributo es un \"si\", eso quiere decir que el alimento es fabricado (hay ciertos pacientes que NO pueden cosumir estos alimentos)\n" +
                "* Atributo \"Apto para celiacos\": en los casos donde este atributo sea un \"si\", si el paciente es CELIACO, entonces este alimento es valido para usar\n" +
                "* Evita repetir los mismos alimentos que propusiste en respuestas anteriores\n" +
                "* En lo posible agregar un postre en almuerzo y/o cena\n" +
                "* No omitas ninguna de las comidas. Tu respuesta debe tener si o si un DESAYUNO, un ALMUERZO," +
                " una MERIENDA y una CENA\n" +
                "* Cada comida debe contener de 3 a 5 alimentos\n\n";
        return this;
    }

    public PromptBuilder agregarDataAlimentos(String json) {
        prompt += "Dataset de alimentos (informacion por 100 gr):\n" + json;
        return this;
    }

    public PromptBuilder agregarConsultaDeMenu(Map<TipoComida, ArrayList<String>> alimentos, Paciente paciente, boolean verbose){
        if(!prompt.isEmpty()){
            prompt = "";
        }
        String respuestaDetallada = verbose ? "" : "en no mas de 10 palabras";
        prompt += "Recomendame platos de comida " + respuestaDetallada + " para consumo de una persona:\n";
        StringBuilder sb = new StringBuilder();
        for (TipoComida tipo : alimentos.keySet()) {
            sb.append("Plato para un/una ").append(tipo.name()).append(" que tenga los siguientes alimentos: ");
            for(String nombre : alimentos.get(tipo)){
                sb.append(nombre.replaceAll(",", "")).append(", ");
            }
            sb.append("\n");
        }
        prompt += sb.toString();
        prompt += "Es importante que cada plato resultante contenga TODOS los alimentos que te pedi " +
                "para su respectiva comida (no podes omitir ninguno).\n";

        if (!paciente.getPatologias().isEmpty()){
            agregarPatologias(paciente.getPatologias());
            prompt += "Trata de cumplir con estas restricciones a la hora de crear un plato de comida.\n";
        }

        return this;
    }

    public void completarComidasFaltantes(int tamanioDado){
        prompt = "Solo me recomendaste alimentos para " + tamanioDado + " comida/s cuando yo te pedi" +
                " para 4 comidas (desayuno, almuerzo, merienda y cena).\n" +
                "Te pido que me vuelvas a recomendar agregando alimentos en las comidas faltantes.\n" +
                "Acordate que el formato que quiero que me devuelvas es:\n" +
                "{\"comidas\":[{\"tipoComida\":...,\"alimentos\":[{\"nombre\":...,\"cantidad\":...},{...}]},{...}]}\n" +
                "No puede haber ningun campo de mas en tu respuesta, solo los que te mencione en la oracion anterior.\n";
    }

}
