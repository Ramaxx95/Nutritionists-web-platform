package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlergiaHandlerManager {

    private final Map<String, AlergiaHandler> handlers = new HashMap<>();
    private final Map<String, AlergiaHandlerConEntrada> handlersConEntrada = new HashMap<>();

    public AlergiaHandlerManager(
        AlergiaPescadoHandler pescadoHandler,
        AlergiaNuezHandler nuezHandler,
        AlergiaManiHandler maniHandler,
        AlergiaHuevoHandler huevoHandler,
        AlergiaSojaHandler sojaHandler,
        AlergiaSesamoHandler sesamoHandler,
        AlergiaTomateHandler tomateHandler,
        AlergiaCerealesGlutenHandler cerealesGlutenHandler,
        AlergiaOtrosAlimentosHandler otrosHandler
    ) {
        handlers.put("Alergia al pescado", pescadoHandler);
        handlers.put("Alergia a la nuez", nuezHandler);
        handlers.put("Alergia al mani", maniHandler);
        handlers.put("Alergia al huevo", huevoHandler);
        handlers.put("Alergia a la soja", sojaHandler);
        handlers.put("Alergia al sésamo", sesamoHandler);
        handlers.put("Alergia al tomate", tomateHandler);
        handlers.put("Alergia a los cereales con gluten", cerealesGlutenHandler);
        handlersConEntrada.put("Alergia a otros alimentos", otrosHandler);
    }

    public List<Alimento> procesarAlergia(String nombreAlergia) {
        AlergiaHandler handler = handlers.get(nombreAlergia);
        if (handler == null) {
            throw new IllegalArgumentException("No hay handler definido para la alergia: " + nombreAlergia);
        }
        return handler.obtenerAlimentosAlergenos();
    }

    public List<Alimento> procesarAlergia(String nombreAlergia, List<String> alergenos) {
        return handlersConEntrada.get(nombreAlergia).obtenerAlimentosAlergenos(alergenos);
    }

    public boolean requiereEntrada(String nombreAlergia) {
        return handlersConEntrada.containsKey(nombreAlergia);
    }
}