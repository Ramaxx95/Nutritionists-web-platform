package com.inutri.dto.paciente;

import org.springframework.lang.Nullable;
import com.inutri.modelo.enums.ActividadFisica;
import com.inutri.validacion.MayorDeEdad;

import lombok.*;


import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ActualizacionDatosPacienteRequest {
    
    @Nullable
    private String nombre;
    @Nullable
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
    private Float altura;
    @Nullable
    private Float peso;
    @Nullable
    private String objetivo;
    @Nullable
    private Float pesoObjetivo;
    private List<Integer> patologias;
    @Nullable
    private List<String> alergenos;
    @Nullable
    private PreferenciasRequest preferencias;

    public ActualizacionDatosPacienteRequest(String nombre, String apellido, String telefono, String mail, ActividadFisica actividadFisica, String sexo, LocalDate fechaNacimiento, Float altura, Float peso, String objetivo, Float pesoObjetivo, List<Integer> patologias, List<String> alergenos, PreferenciasRequest preferencias) {
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
        this.preferencias = preferencias;
    }
}