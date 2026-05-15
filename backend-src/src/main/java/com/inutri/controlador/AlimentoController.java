package com.inutri.controlador;

import com.inutri.dto.alimento.*;
import com.inutri.servicio.AlimentoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AlimentoController {

    @Autowired
    private AlimentoService alimentoService;

    @Operation(summary = "Listado de alimentos")
    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de alimentos")
    @GetMapping("/alimentos")
    public ResponseEntity<Page<AlimentoListadoResponse>> listarAlimentos(
        @ModelAttribute AlimentoListadoRequest filtros,
        @PageableDefault(size = 20, sort = "id") Pageable pageable,
        Authentication authentication
    ) {
        String mailUsuario = authentication.getName();
        Page<AlimentoListadoResponse> listado = alimentoService.listarAlimentos(pageable, filtros, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(listado);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el alimento solicitado")
    @GetMapping("/alimentos/{id}")
    public ResponseEntity<AlimentoDetalleResponse> buscarAlimento(
            @PathVariable("id") Integer id,
            Authentication authentication) {
        String mailUsuario = authentication.getName();

        AlimentoDetalleResponse alimento = alimentoService.obtenerAlimento(id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alimento);
    }

    @ApiResponse(responseCode = "201", description = "Alimento registrado exitosamente")
    @PostMapping("/alimentos")
    public ResponseEntity<AgregarAlimentoResponse> registrarAlimento(
            @Valid @RequestBody AgregarAlimentoRequest alimentoRequestDTO,
            Authentication authentication) {
        String mailUsuario = authentication.getName();

        AgregarAlimentoResponse alimento = alimentoService.agregarAlimentoPersonalizado(alimentoRequestDTO, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alimento);
    }

    @ApiResponse(responseCode = "200", description = "Alimento actualizado exitosamente")
    @PutMapping("/alimentos/{id}")
    public ResponseEntity<AlimentoDetalleResponse> actualizarAlimentoPersonalizado(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarAlimentoRequest actualizarAlimentoRequestDTO,
            Authentication authentication) {
        String mailUsuario = authentication.getName();

        AlimentoDetalleResponse actualizado = alimentoService.actualizarAlimentoPersonalizado(id, actualizarAlimentoRequestDTO, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(actualizado);
    }

    @ApiResponse(responseCode = "204", description = "Alimento eliminado exitosamente")
    @DeleteMapping("/alimentos/{id}")
    public ResponseEntity<Void> eliminarAlimentoPersonalizado(
            @PathVariable Integer id,
            Authentication authentication) {
        String mailUsuario = authentication.getName();
        
        alimentoService.eliminarAlimentoPersonalizado(id, mailUsuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaListadoResponse>> obtenerCategorias() {
        List<CategoriaListadoResponse> categorias = alimentoService.obtenerCategorias();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(categorias);
    }
}