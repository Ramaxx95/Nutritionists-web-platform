package com.inutri.servicio.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.inutri.servicio.EmailService;
import com.inutri.servicio.UsuarioService;

@Service
@Profile("!test")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRecuperacion(String destinatario, String nombre, String apellido, String token) {
        String asunto = "Recuperación de contraseña - iNutri";
        String url = "http://host-inutri-app.s3-website-us-west-2.amazonaws.com/auth/reset-password?token=" + token;
        String mensaje = "Hola " + nombre + " " + apellido 
                + ",\n\nRecibimos una solicitud para restablecer tu contraseña. "
                + "Podés hacerlo haciendo clic en el siguiente enlace:\n\n" + url
                + "\n\nEste enlace expirará en 20 minutos.\n\nSi no hiciste esta solicitud, ignorá este mensaje.";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destinatario);
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mail.setFrom("inutri.soporte@gmail.com");

        mailSender.send(mail);
    }

    @Override
    public void enviarCorreoVerificacion(String destinatario, String nombre, String apellido, String token) {
        String asunto = "Verificación de correo electrónico - iNutri";
        String url = "http://host-inutri-app.s3-website-us-west-2.amazonaws.com/auth/verificar-email?token=" + token;

        String mensaje = "Hola " + nombre + " " + apellido + ",\n\n"
            + "Gracias por registrarte en iNutri. Para comenzar a usar tu cuenta, por favor verificá tu dirección de correo electrónico "
            + "haciendo clic en el siguiente enlace:\n\n"
            + url + "\n\n"
            + "Este enlace expirará en 24 horas.\n\n"
            + "Si no te registraste en iNutri, podés ignorar este mensaje.";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destinatario);
        mail.setSubject(asunto);
        mail.setText(mensaje);
        mail.setFrom("inutri.soporte@gmail.com");

        mailSender.send(mail);
    }
}