package com.inutri.modelo;

import com.inutri.modelo.enums.TipoSuscripcion;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_subscripcion", nullable = false)
    private TipoSuscripcion tipoSubscripcion = TipoSuscripcion.DEMO;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

    @Column(name = "fecha_ultimo_pago")
    private LocalDateTime fechaUltimoPago;

    @Column(name = "usos_plan_ia_restantes")
    private Integer usosPlanIaRestantes = 20;

    @Column(name = "usos_menu_ia_restantes")
    private Integer usosMenuIaRestantes = 20;

    @Column(name = "ultima_actualizacion_contadores")
    private LocalDateTime ultimaActualizacionContadores = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

    @Column(name = "email_verificado", nullable = false)
    private boolean emailVerificado = false;

    @Column(name = "auto_renovacion_activa", nullable = false)
    private boolean autoRenovacionActiva = false;

    public Usuario(String email, String contraseña, String nombre, String apellido) {
        this.email = email;
        this.password = contraseña;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public boolean esPremiumActivo() {
        if (tipoSubscripcion != TipoSuscripcion.PREMIUM || fechaUltimoPago == null) return false;
        LocalDateTime finPeriodo = fechaUltimoPago.plusDays(30);
        return LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).isBefore(finPeriodo);
    }
}