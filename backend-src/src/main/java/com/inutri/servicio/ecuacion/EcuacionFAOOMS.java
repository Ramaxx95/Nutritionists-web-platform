package com.inutri.servicio.ecuacion;

import com.inutri.exception.paciente.*;
import com.inutri.modelo.Paciente;

import java.math.BigDecimal;

public class EcuacionFAOOMS implements EcuacionStrategy {

    @Override
    public void validarDatosPaciente(Paciente paciente) {
        if (paciente.getSexo() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el género del paciente para calcular la cantidad de Kcal del plan.");
        }
        if (paciente.getPeso() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el peso del paciente para poder usar la ecuación FAO/OMS.");
        }
        if (paciente.getFechaNacimiento() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar la fecha de nacimiento del paciente para poder usar la ecuación FAO/OMS.");
        }
    }

    @Override
    public BigDecimal calcularGER(Paciente paciente) {
        BigDecimal peso = BigDecimal.valueOf(paciente.getPeso());
        int edad = paciente.getEdad();
        boolean esHombre = paciente.getSexo().equalsIgnoreCase("masculino");

        if (edad >= 18 && edad <= 29) {
            if (esHombre) {
                return peso.multiply(BigDecimal.valueOf(15.057)).add(BigDecimal.valueOf(692.2));
            } else {
                return peso.multiply(BigDecimal.valueOf(14.818)).add(BigDecimal.valueOf(486.6));
            }
        } else if (edad >= 30 && edad <= 60) {
            if (esHombre) {
                return peso.multiply(BigDecimal.valueOf(11.472)).add(BigDecimal.valueOf(873.1));
            } else {
                return peso.multiply(BigDecimal.valueOf(8.126)).add(BigDecimal.valueOf(845.6));
            }
        } else if (edad > 60) {
            if (esHombre) {
                return peso.multiply(BigDecimal.valueOf(11.711)).add(BigDecimal.valueOf(587.7));
            } else {
                return peso.multiply(BigDecimal.valueOf(9.082)).add(BigDecimal.valueOf(658.5));
            }
        } else {
            throw new DatosPacienteInvalidosException("La ecuación FAO/OMS solo está definida para pacientes mayores de 18 años.");
        }
    }
}