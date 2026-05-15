package com.inutri.servicio.ecuacion;

import com.inutri.exception.paciente.DatosPacienteFaltantesException;
import com.inutri.modelo.Paciente;
import java.math.BigDecimal;

public class EcuacionHarrisBenedict implements EcuacionStrategy {

    @Override
    public void validarDatosPaciente(Paciente paciente) {
        if (paciente.getSexo() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el género del paciente para calcular la cantidad de Kcal del plan.");
        }
        if (paciente.getPeso() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar el peso del paciente para poder usar la ecuación Harris-Benedict.");
        }
        if (paciente.getAltura() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar la altura del paciente para poder usar la ecuación Harris-Benedict.");
        }
        if (paciente.getFechaNacimiento() == null) {
            throw new DatosPacienteFaltantesException("Se necesita cargar la fecha de nacimiento del paciente para poder usar la ecuación Harris-Benedict.");
        }
    }

    @Override
    public BigDecimal calcularGER(Paciente paciente) {
        BigDecimal peso = new BigDecimal(paciente.getPeso());
        BigDecimal altura = new BigDecimal(paciente.getAltura());
        BigDecimal edad = new BigDecimal(paciente.getEdad());
        if (paciente.getSexo().equalsIgnoreCase("masculino")) {
            return BigDecimal.valueOf(66)
                .add(peso.multiply(BigDecimal.valueOf(13.7)))
                .add(altura.multiply(BigDecimal.valueOf(5)))
                .subtract(edad.multiply(BigDecimal.valueOf(6.8)));
        } else {
            return BigDecimal.valueOf(655)
                .add(peso.multiply(BigDecimal.valueOf(9.7)))
                .add(altura.multiply(BigDecimal.valueOf(1.8)))
                .subtract(edad.multiply(BigDecimal.valueOf(4.7)));
        }
    }
}