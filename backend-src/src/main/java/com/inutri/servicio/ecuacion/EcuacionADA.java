package com.inutri.servicio.ecuacion;

import com.inutri.exception.paciente.DatosPacienteFaltantesException;
import com.inutri.modelo.Paciente;
import java.math.BigDecimal;

public class EcuacionADA implements EcuacionStrategy {

    @Override
    public void validarDatosPaciente(Paciente paciente) {
        if (paciente.getSexo() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el género del paciente para calcular la cantidad de Kcal del plan.");
        }
        if (paciente.getPeso() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el peso del paciente para poder usar la ecuación ADA.");
        }
    }

    @Override
    public BigDecimal calcularGER(Paciente paciente) {
        BigDecimal peso = new BigDecimal(paciente.getPeso());
        if (paciente.getSexo().equalsIgnoreCase("masculino")) {
            return peso.multiply(BigDecimal.valueOf(24));
        } else {
            return BigDecimal.valueOf(0.95).multiply(peso.multiply(BigDecimal.valueOf(24)));
        }
    }
}