package com.inutri.servicio;

public interface EmailService {
    void enviarCorreoRecuperacion(String destinatario, String nombre, String apellido, String token);
    void enviarCorreoVerificacion(String destinatario, String nombre, String apellido, String token);
}