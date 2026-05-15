package com.inutri.modelo;

import com.inutri.modelo.converters.ActividadFisicaConverter;
import com.inutri.modelo.converters.AlergenosPersonalizadosConverter;
import com.inutri.modelo.enums.ActividadFisica;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic(optional = false)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Basic
    @Column(name = "telefono")
    private String telefono;

    @Basic
    @Column(name = "email_paciente")
    private String mail;

    @Convert(converter = ActividadFisicaConverter.class)
    @Column(name = "actividad_fisica")
    private ActividadFisica actividadFisica;

    @Basic
    @Column(name = "sexo")
    private String sexo;

    @Basic
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Basic
    @Column(name = "altura")
    private Float altura;

    @Basic
    @Column(name = "peso")
    private Float peso;

    @Basic(optional = false)
    @Column(name = "objetivo", nullable = false)
    private String objetivo;

    @Basic
    @Column(name = "peso_objetivo")
    private Float pesoObjetivo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "pacientes_patologias",
        joinColumns = @JoinColumn(name = "paciente_id"),
        inverseJoinColumns = @JoinColumn(name = "patologia_id")
    )
    private List<Patologia> patologias = new ArrayList<>();

    @Convert(converter = AlergenosPersonalizadosConverter.class)
    @Column(name = "alergenos_personalizados", columnDefinition = "TEXT")
    private List<String> alergenosPersonalizados = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "pacientes_alergenos",
        joinColumns = @JoinColumn(name = "paciente_id"),
        inverseJoinColumns = @JoinColumn(name = "alimento_id")
    )
    private List<Alimento> alergias = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "preferencias_gustos",
            joinColumns = @JoinColumn(name = "paciente_id"),
            inverseJoinColumns = @JoinColumn(name = "alimento_preferido_id")
    )
    private List<Alimento> alimentosPreferidos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "preferencias_disgustos",
            joinColumns = @JoinColumn(name = "paciente_id"),
            inverseJoinColumns = @JoinColumn(name = "alimento_restringido_id")
    )
    private List<Alimento> alimentosRestringidos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    public Paciente(
            String nombre,
            String apellido,
            String telefono,
            String mail,
            ActividadFisica actividadFisica,
            String sexo,
            LocalDate fechaNacimiento,
            Float altura,
            Float peso,
            String objetivo,
            float pesoObjetivo,
            List<Patologia> patologias,
            List<Alimento> gustos,
            List<Alimento> disgustos,
            Usuario usuario) {
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
        this.patologias = patologias != null ? new ArrayList<>(patologias) : new ArrayList<>();
        this.alimentosPreferidos = gustos != null ? new ArrayList<>(gustos) : new ArrayList<>();
        this.alimentosRestringidos = disgustos != null ? new ArrayList<>(disgustos) : new ArrayList<>();
        this.usuario = usuario;
    }

    public void agregarAlergia(Alimento alimento) {
        if (alergias == null) {
            alergias = new ArrayList<>();
        }
        if (!alergias.contains(alimento)) {
            alergias.add(alimento);
        }
    }

    public void quitarAlergia(Alimento alimento) {
        alergias.remove(alimento);
    }

    public int getEdad() {
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }
}