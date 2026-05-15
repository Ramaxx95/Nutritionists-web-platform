package com.inutri.servicio.ecuacion;

import com.inutri.modelo.Paciente;
import java.math.BigDecimal;

public interface EcuacionStrategy {
    void validarDatosPaciente(Paciente paciente);
    BigDecimal calcularGER(Paciente paciente);
}