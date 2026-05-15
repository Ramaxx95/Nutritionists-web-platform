package com.inutri.servicio.impl;

import com.inutri.dto.usuario.*;
import com.inutri.exception.suscripcion.*;
import com.inutri.exception.usuario.*;
import com.inutri.modelo.EmailVerificationToken;
import com.inutri.modelo.PasswordResetToken;
import com.inutri.modelo.Usuario;
import com.inutri.modelo.enums.TipoSuscripcion;
import com.inutri.repositorio.EmailVerificationTokenRepository;
import com.inutri.repositorio.PasswordResetTokenRepository;
import com.inutri.repositorio.UsuarioRepository;
import com.inutri.servicio.EmailService;
import com.inutri.servicio.UsuarioService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRecuperacionRepository;

    @Autowired
    private EmailVerificationTokenRepository tokenVerificacionRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public RegistroUsuarioResponse registrar(RegistroUsuarioRequest dto) {
        if (!dto.getContraseña().equals(dto.getConfirmarContraseña())) {
            throw new ContraseñasNoCoincidenException("Las contraseñas ingresadas no coinciden.");
        }
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UsuarioYaExistenteException("El email ya está registrado en el sistema.");
        }

        Usuario usuario = toEntity(dto);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now().plusHours(24);
        EmailVerificationToken tokenVerificacion = new EmailVerificationToken(token, usuarioGuardado, expiracion);
        tokenVerificacionRepository.save(tokenVerificacion);

        emailService.enviarCorreoVerificacion(usuarioGuardado.getEmail(), usuarioGuardado.getNombre(), usuarioGuardado.getApellido(), token);

        return toRegistroResponse(usuarioGuardado);
    }

    @Override
    @Transactional
    public void verificarEmail(String token) {
        EmailVerificationToken tokenVerificacion = tokenVerificacionRepository.findByToken(token)
            .orElseThrow(() -> new TokenNoValidoException("Token inválido o no encontrado."));

        if (tokenVerificacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new TokenExpiradoException("El token ha expirado.");
        }

        Usuario usuario = tokenVerificacion.getUsuario();
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        tokenVerificacionRepository.deleteByUsuarioId(usuario.getId());
    }

    @Override
    public LoginUsuarioResponse iniciarSesion(LoginUsuarioRequest usuarioDto){
        Usuario usuarioGuardado = usuarioRepository.findByEmail(usuarioDto.getEmail())
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario o contraseña incorrectos."));
        if(!passwordEncoder.matches(usuarioDto.getContraseña(), usuarioGuardado.getPassword())){
            throw new ContraseñaErroneaException("Usuario o contraseña incorrectos.");
        }
        if (!usuarioGuardado.isEmailVerificado()) {
            throw new EmailNoVerificadoException("Debes verificar tu correo electrónico antes de iniciar sesión.");
        }
        boolean actualizado = false;
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        if (usuarioGuardado.getTipoSubscripcion() == TipoSuscripcion.DEMO) {
            LocalDateTime fechaFinDemo = usuarioGuardado.getFechaRegistro().plusDays(30);
            if (ahora.isAfter(fechaFinDemo)) {
                usuarioGuardado.setTipoSubscripcion(TipoSuscripcion.FREE);
                actualizado = true;
            }
        } else if (usuarioGuardado.getTipoSubscripcion() == TipoSuscripcion.PREMIUM) {
            LocalDateTime fechaUltimoPago = usuarioGuardado.getFechaUltimoPago();
            if (fechaUltimoPago != null) {
                LocalDateTime fechaFinPremium = fechaUltimoPago.plusDays(30);
                if (ahora.isAfter(fechaFinPremium)) {
                    if (usuarioGuardado.isAutoRenovacionActiva()) {
                        long ciclosVencidos = ChronoUnit.MONTHS.between(fechaUltimoPago, ahora);
                        LocalDateTime nuevaFechaPago = fechaUltimoPago.plusMonths(ciclosVencidos);
                        usuarioGuardado.setFechaUltimoPago(nuevaFechaPago);
                    } else {
                        usuarioGuardado.setTipoSubscripcion(TipoSuscripcion.FREE);
                    }
                    actualizado = true;
                }
            }
        }
        if (actualizado) {
            usuarioGuardado.setUsosMenuIaRestantes(20);
            usuarioGuardado.setUsosPlanIaRestantes(20);
            usuarioGuardado.setUltimaActualizacionContadores(ahora);
            usuarioRepository.save(usuarioGuardado);
        }

        return toLoginResponse(usuarioGuardado);
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNoEncontradoException("No existe un usuario registrado con este mail."));
    }

    @Override
    public void solicitarRecuperacionPassword(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNoEncontradoException("No existe un usuario con ese email."));

        tokenRecuperacionRepository.findAll().stream()
            .filter(t -> t.getUsuario().getId().equals(usuario.getId()))
            .forEach(tokenRecuperacionRepository::delete);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracion = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusMinutes(20);

        PasswordResetToken resetToken = new PasswordResetToken(token, usuario, expiracion);
        tokenRecuperacionRepository.save(resetToken);

        emailService.enviarCorreoRecuperacion(usuario.getEmail(), usuario.getNombre(), usuario.getApellido(), token);
    }

    @Override
    public Usuario validarTokenResetPassword(String token) {
        PasswordResetToken resetToken = tokenRecuperacionRepository.findByToken(token)
            .orElseThrow(() -> new TokenNoValidoException("Token inválido"));

        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))) {
            tokenRecuperacionRepository.delete(resetToken);
            throw new TokenExpiradoException("El token ya ha expirado");
        }

        return resetToken.getUsuario();
    }

    @Override
    public void resetearPassword(String token, String nuevaContraseña, String confirmarContraseña) {
        if (!nuevaContraseña.equals(confirmarContraseña)) {
            throw new ContraseñasNoCoincidenException("Las contraseñas no coinciden");
        }

        PasswordResetToken resetToken = tokenRecuperacionRepository.findByToken(token)
            .orElseThrow(() -> new TokenNoValidoException("Token inválido"));

        if (resetToken.getFechaExpiracion().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))) {
            tokenRecuperacionRepository.delete(resetToken);
            throw new TokenExpiradoException("El token ha expirado");
        }

        Usuario usuario = resetToken.getUsuario();

        String encodedPassword = passwordEncoder.encode(nuevaContraseña);
        usuario.setPassword(encodedPassword);

        usuarioRepository.save(usuario);
        tokenRecuperacionRepository.delete(resetToken);
    }

    @Override
    public SuscripcionUsuarioResponse activarPremium(Long id, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        if (!usuario.getId().equals(id)) {
            throw new AccesoNoAutorizadoException("No puede modificar la suscripción de otro usuario.");
        }

        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        if (usuario.getTipoSubscripcion() == TipoSuscripcion.PREMIUM && usuario.isAutoRenovacionActiva()) {
            int diasRestantes = (int) ChronoUnit.DAYS.between(ahora, usuario.getFechaUltimoPago().plusDays(30));
            return toSuscripcionResponse(usuario, Math.max(diasRestantes, 0));
        }
        if (usuario.getTipoSubscripcion() == TipoSuscripcion.PREMIUM) {
            usuario.setAutoRenovacionActiva(true);
            Usuario guardado = usuarioRepository.save(usuario);
            int diasRestantes = (int) ChronoUnit.DAYS.between(ahora, usuario.getFechaUltimoPago().plusDays(30));
            return toSuscripcionResponse(guardado, Math.max(diasRestantes, 0));
        }

        usuario.setTipoSubscripcion(TipoSuscripcion.PREMIUM);
        usuario.setFechaUltimoPago(ahora);
        usuario.setAutoRenovacionActiva(true);
        Usuario guardado = usuarioRepository.save(usuario);

        int diasRestantes = 30;
        return toSuscripcionResponse(guardado, diasRestantes);
    }

    @Override
    public void cancelarPremium(Long id, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        if (!usuario.getId().equals(id)) {
            throw new AccesoNoAutorizadoException("No puede modificar la suscripción de otro usuario.");
        }

        if (usuario.getTipoSubscripcion() != TipoSuscripcion.PREMIUM) {
            throw new UsuarioNoPremiumException("El usuario no tiene una suscripción premium activa.");
        }

        usuario.setAutoRenovacionActiva(false);
        usuarioRepository.save(usuario);
    }

    public UsosRestantesResponse obtenerUsosPlanesRestantes(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));
        resetearContadoresSiCorresponde(usuario);

        return new UsosRestantesResponse(
                usuario.getUsosPlanIaRestantes(),
                usuario.getUltimaActualizacionContadores()
        );
    }

    public UsosRestantesResponse obtenerUsosMenusRestantes(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado."));
        resetearContadoresSiCorresponde(usuario);

        return new UsosRestantesResponse(
                usuario.getUsosMenuIaRestantes(),
                usuario.getUltimaActualizacionContadores()
        );
    }

    private Usuario toEntity(RegistroUsuarioRequest dto) {
        String passwordEncriptada = passwordEncoder.encode(dto.getContraseña());
        return new Usuario(dto.getEmail(), passwordEncriptada, dto.getNombre(), dto.getApellido());
    }

    private RegistroUsuarioResponse toRegistroResponse(Usuario usuario) {
        return new RegistroUsuarioResponse(usuario.getId(), usuario.getEmail(), usuario.getNombre(), usuario.getApellido());
    }

    private LoginUsuarioResponse toLoginResponse(Usuario usuario) {
        TipoSuscripcion tipo = usuario.getTipoSubscripcion();
        Integer diasRestantes = null;
        if (tipo == TipoSuscripcion.DEMO) {
            diasRestantes = calcularDiasRestantes(usuario.getFechaRegistro());
        } else if (tipo == TipoSuscripcion.PREMIUM && usuario.getFechaUltimoPago() != null) {
            diasRestantes = calcularDiasRestantes(usuario.getFechaUltimoPago());
        }
        return new LoginUsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getApellido(), tipo, diasRestantes);
    }

    private SuscripcionUsuarioResponse toSuscripcionResponse(Usuario usuario, Integer diasRestantes) {
        return new SuscripcionUsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getTipoSubscripcion(), usuario.getFechaUltimoPago(), diasRestantes, usuario.isAutoRenovacionActiva());
    }

    private int calcularDiasRestantes(LocalDateTime fechaInicio) {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime fechaFin = fechaInicio.plusDays(30);
        if (fechaFin.isBefore(ahora)) {
            return 0;
        }
        return (int) java.time.Duration.between(ahora, fechaFin).toDays();
    }

    public void resetearContadoresSiCorresponde(Usuario usuario) {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime ultimaActualizacion = usuario.getUltimaActualizacionContadores();

        if (ultimaActualizacion == null || cambioDeMes(ultimaActualizacion, ahora)) {
            usuario.setUsosMenuIaRestantes(20);
            usuario.setUsosPlanIaRestantes(20);
            usuario.setUltimaActualizacionContadores(ahora);
            usuarioRepository.save(usuario);
        }
    }

    private boolean cambioDeMes(LocalDateTime ultima, LocalDateTime actual) {
        return ultima.getMonth() != actual.getMonth() || ultima.getYear() != actual.getYear();
    }
}