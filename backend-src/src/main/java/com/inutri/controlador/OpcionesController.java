package com.inutri.controlador;

import com.inutri.dto.laboratorio.BiomarcadorResponse;
import com.inutri.dto.patologia.PatologiaResponse;
import com.inutri.modelo.enums.*;
import com.inutri.servicio.LaboratorioService;
import com.inutri.servicio.PatologiaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/opciones")
public class OpcionesController {

    @Autowired
    private PatologiaService patologiaService;

    @Autowired
    private LaboratorioService laboratorioService;

    @GetMapping("/ecuaciones")
    public ResponseEntity<List<String>> obtenerEcuaciones() {
        List<String> ecuaciones = Arrays.stream(EcuacionGER.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ecuaciones);
    }

    @GetMapping("/distribuciones")
    public ResponseEntity<List<String>> obtenerDistribuciones() {
        List<String> distribuciones = Arrays.stream(TipoDistribucion.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(distribuciones);
    }

    @GetMapping("/actividad-fisica")
    public ResponseEntity<List<String>> obtenerNivelesActividad() {
        List<String> nivelesActividad = Arrays.stream(ActividadFisica.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(nivelesActividad);        
    }

    @GetMapping("/patologias")
    public ResponseEntity<List<PatologiaResponse>> listarPatologiasConCriterios() {
        List<PatologiaResponse> patologias = patologiaService.listarConCriterios();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(patologias);
    }

    @GetMapping("/biomarcadores")
    public ResponseEntity<List<BiomarcadorResponse>> listarBiomarcadoresConRangos() {
        List<BiomarcadorResponse> biomarcadores = laboratorioService.obtenerBiomarcadoresConRangos();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(biomarcadores);
    }
}