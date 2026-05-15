package com.inutri.dto.paciente;

import com.inutri.dto.laboratorio.AnalisisLaboratorioRequest;
import com.inutri.modelo.enums.ActividadFisica;

import org.springframework.lang.Nullable;
import com.inutri.validacion.MayorDeEdad;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class RegistroPacienteRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String nombre;
    @NotBlank(message = "El apellido no puede estar vacío.")
    private String apellido;
    @Nullable
    private String telefono;
    @Nullable
    private String mail;
    @Nullable
    private ActividadFisica actividadFisica;
    @Nullable
    private String sexo;
    @Nullable
    @MayorDeEdad
    private LocalDate fechaNacimiento;
    @Nullable
    private float altura;
    @Nullable
    private float peso;
    @NotBlank(message = "El objetivo no puede estar vacío.")
    private String objetivo;
    @Nullable
    private float pesoObjetivo;
    private List<Integer> patologias;
    @Nullable
    private List<String> alergenos;
    @Valid
    private List<AnalisisLaboratorioRequest> analisis;
    @Nullable
    private PreferenciasRequest preferencias;

    public RegistroPacienteRequest(String nombre, String apellido, String telefono, String mail, ActividadFisica actividadFisica, String sexo, LocalDate fechaNacimiento, float altura, float peso, String objetivo, float pesoObjetivo, List<Integer> patologias, List<String> alergenos, List<AnalisisLaboratorioRequest> analisis, PreferenciasRequest preferencias) {
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
        this.analisis = analisis;
        this.preferencias = preferencias;
    }
}