package com.inutri.dto.paciente;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.inutri.dto.alimento.AlergiaAlimentoResponse;
import com.inutri.modelo.enums.ActividadFisica;

@Getter
@NoArgsConstructor
public class RegistroPacienteResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String mail;
    private ActividadFisica actividadFisica;
    private String sexo;
    private LocalDate fechaNacimiento;
    private float altura;
    private float peso;
    private String objetivo;
    private float pesoObjetivo;
    private List<String> patologias;
    private List<String> alergenos;
    private List<AlergiaAlimentoResponse> alergias;
    private PreferenciasResponse preferencias;
    private LocalDateTime ultimaSesion;
    private LocalDateTime proximaSesion;

    public RegistroPacienteResponse (Long id, String nombre, String apellido, String telefono, String mail, ActividadFisica actividadFisica, String sexo, LocalDate fechaNacimiento, float altura, float peso, String objetivo, float pesoObjetivo, List<String> patologias, List<String> alergenos, List<AlergiaAlimentoResponse> alergias, PreferenciasResponse preferencias, LocalDateTime ultimaSesion, LocalDateTime proximaSesion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.mail = mail;
        this.actividadFisica = actividadFisica;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.altura = altura;
        this.peso = peso;
        this.objetivo = objetivo;
        this.pesoObjetivo = pesoObjetivo;
        this.patologias = patologias;
        this.alergenos = alergenos;
        this.alergias = alergias;
        this.preferencias = preferencias;
        this.ultimaSesion = ultimaSesion;
        this.proximaSesion = proximaSesion;
    }
}