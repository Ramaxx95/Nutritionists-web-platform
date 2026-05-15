package com.inutri.servicio.ecuacion;

import com.inutri.modelo.enums.EcuacionGER;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class EcuacionStrategyFactory {

    private final Map<EcuacionGER, EcuacionStrategy> estrategias = new EnumMap<>(EcuacionGER.class);

    public EcuacionStrategyFactory() {
        estrategias.put(EcuacionGER.ADA, new EcuacionADA());
        estrategias.put(EcuacionGER.FAO_OMS, new EcuacionFAOOMS());
        estrategias.put(EcuacionGER.HARRIS_BENEDICT, new EcuacionHarrisBenedict());
    }

    public EcuacionStrategy getEstrategia(EcuacionGER ecuacion) {
        return estrategias.get(ecuacion);
    }
}