package com.inutri.exception;

import com.inutri.exception.usuario.*;
import com.inutri.exception.alimento.*;
import com.inutri.exception.laboratorio.*;
import com.inutri.exception.paciente.*;
import com.inutri.exception.patologia.*;
import com.inutri.exception.plan.*;
import com.inutri.exception.sesion.EstadoNoValidoException;
import com.inutri.exception.sesion.FechaPasadaException;
import com.inutri.exception.sesion.SesionNoEncontradaException;
import com.inutri.exception.suscripcion.SoloPremiumException;
import com.inutri.exception.suscripcion.UsuarioNoPremiumException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import org.springframework.http.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioYaExistenteException.class)
    public ResponseEntity<String> manejarUsuarioDuplicado(UsuarioYaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarArgumentoInvalido(MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getDefaultMessage())
            .collect(Collectors.joining(";"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ContraseñasNoCoincidenException.class)
    public ResponseEntity<String> manejarContraseñasNoCoinciden(ContraseñasNoCoincidenException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ContraseñaErroneaException.class)
    public ResponseEntity<String> manejarContraseñasErronea(ContraseñaErroneaException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<String> manejarPacienteNoEncontrado(PacienteNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ComidasFaltantesException.class)
    public ResponseEntity<String> manejarComidasFaltantes(ComidasFaltantesException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AlimentoNoEncontradoException.class)
    public ResponseEntity<String> manejarAlimentoNoEncontrado(AlimentoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DatosPacienteInvalidosException.class)
    public ResponseEntity<String> manejarDatosDePacienteInvalidos(DatosPacienteInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DatosPacienteFaltantesException.class)
    public ResponseEntity<String> manejarDatosDePacienteFaltantes(DatosPacienteFaltantesException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(PlanNoEncontradoException.class)
    public ResponseEntity<String> manejarPlanNoEncontrado(PlanNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlternativaNoEncontradaException.class)
    public ResponseEntity<String> manejarAlternativaNoEncontrada(AlternativaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MenuNoEncontradoException.class)
    public ResponseEntity<String> manejarMenuNoEncontrado(MenuNoEncontradoException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MenuInvalidoException.class)
    public ResponseEntity<String> manejarMenuInvalido(MenuInvalidoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DistribucionInvalidaException.class)
    public ResponseEntity<String> manejarDistribucionInvalida(DistribucionInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PatologiaNoEncontradaException.class)
    public ResponseEntity<String> manejarPatologiaNoEncontrada(PatologiaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AlimentoYaExistenteException.class)
    public ResponseEntity<String> manejarPlanYaExistente(AlimentoYaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(BiomarcadorNoEncontradoException.class)
    public ResponseEntity<String> manejarPatologiaNoEncontrada(BiomarcadorNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SesionNoEncontradaException.class)
    public ResponseEntity<String> manejarSesionNoEncontrada(SesionNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EstadoNoValidoException.class)
    public ResponseEntity<String> manejarEstadoNoValido(EstadoNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<String> manejarAccesoNoAutorizado(AccesoNoAutorizadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(SoloPremiumException.class)
    public ResponseEntity<String> manejarSoloPremium(SoloPremiumException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(UsosAgotadosException.class)
    public ResponseEntity<String> manejarUsosAgotados(UsosAgotadosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TokenNoValidoException.class)
    public ResponseEntity<String> manejarTokenNoValido(TokenNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<String> manejarTokenExpirado(TokenExpiradoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EmailNoVerificadoException.class)
    public ResponseEntity<String> manejarEmailNoVerificado(EmailNoVerificadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(FechaPasadaException.class)
    public ResponseEntity<String> manejarFechaPasada(FechaPasadaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoPremiumException.class)
    public ResponseEntity<String> manejarUsuarioNoPremium(UsuarioNoPremiumException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}