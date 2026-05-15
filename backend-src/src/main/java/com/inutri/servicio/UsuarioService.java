package com.inutri.servicio;

import com.inutri.dto.usuario.*;
import com.inutri.modelo.Usuario;

public interface UsuarioService {
    RegistroUsuarioResponse registrar(RegistroUsuarioRequest usuario);
    void verificarEmail(String token);
    LoginUsuarioResponse iniciarSesion(LoginUsuarioRequest usuario);
    Usuario obtenerPorEmail(String email);
    void solicitarRecuperacionPassword(String email);
    Usuario validarTokenResetPassword(String token);
    void resetearPassword(String token, String nuevaContraseña, String confirmarContraseña);
    SuscripcionUsuarioResponse activarPremium(Long id, String mailUsuario);
    void cancelarPremium(Long id, String mailUsuario);
    UsosRestantesResponse obtenerUsosPlanesRestantes(String email);
    UsosRestantesResponse obtenerUsosMenusRestantes(String email);   
    void resetearContadoresSiCorresponde(Usuario usuario);
}