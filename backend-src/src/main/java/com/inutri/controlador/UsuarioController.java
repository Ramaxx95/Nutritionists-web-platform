package com.inutri.controlador;

import com.inutri.dto.usuario.*;
import com.inutri.modelo.Usuario;
import com.inutri.servicio.JwtService;
import com.inutri.servicio.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<RegistroUsuarioResponse> crearUsuario(@Valid @RequestBody RegistroUsuarioRequest usuario) {
        RegistroUsuarioResponse usuarioNuevo = usuarioService.registrar(usuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioNuevo);
    }

    @Operation(summary = "Verificar el email del usuario")
    @ApiResponse(responseCode = "200", description = "Email verificado correctamente")
    @PostMapping("/verificar-email")
    public ResponseEntity<Void> verificarEmail(@RequestParam String token) {
        usuarioService.verificarEmail(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Iniciar sesion con un usuario")
    @ApiResponse(responseCode = "201", description = "Inicio de sesión exitoso")
    @PostMapping("/login")
    public ResponseEntity<LoginUsuarioResponse> iniciarSesion(@Valid @RequestBody LoginUsuarioRequest usuario) {
        LoginUsuarioResponse usuarioIniciado = usuarioService.iniciarSesion(usuario);
        String token = jwtService.generateToken(usuario.getEmail());
        usuarioIniciado.setToken(token);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(usuarioIniciado);
    }

    @GetMapping("/{email}")
    public ResponseEntity<RegistroUsuarioResponse> getUsuario(@PathVariable String email) {
        Usuario usuario = usuarioService.obtenerPorEmail(email);
        RegistroUsuarioResponse usuarioDTO = new RegistroUsuarioResponse(usuario.getId(), usuario.getEmail(), usuario.getNombre(), usuario.getApellido());
        return ResponseEntity.ok(usuarioDTO);
    }

    @Operation(summary = "Solicitar recuperación de contraseña")
    @ApiResponse(responseCode = "200", description = "Solicitud procesada correctamente")
    @PostMapping("/recuperar-password")
    public ResponseEntity<Void> solicitarRecuperacionPassword(@Valid @RequestBody RecuperarPasswordRequest request) {
        usuarioService.solicitarRecuperacionPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Validar que el token sea valido")
    @ApiResponse(responseCode = "200", description = "Token valido")
    @PostMapping("/reset-password/validar-token")
    public ResponseEntity<Void> validarToken(@RequestParam String token) {
        usuarioService.validarTokenResetPassword(token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Solicitar reseteo de contraseña")
    @ApiResponse(responseCode = "200", description = "Reseteo de contraseña exitoso")
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetearPassword(@Valid @RequestBody ResetPasswordRequest request) {
        usuarioService.resetearPassword(request.getToken(), request.getNuevaContraseña(), request.getConfirmarContraseña());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Activar la suscripcion premium a un usuario")
    @ApiResponse(responseCode = "200", description = "Suscripcion activada exitosamente")
    @PostMapping("/{id}/premium")
    public ResponseEntity<SuscripcionUsuarioResponse> activarPremium(@PathVariable Long id, Authentication authentication) {
        String mailUsuario = authentication.getName();
        SuscripcionUsuarioResponse response = usuarioService.activarPremium(id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Cancelar la renovación automática de la suscripción premium")
    @ApiResponse(responseCode = "200", description = "Renovación automática cancelada correctamente")
    @DeleteMapping("/{id}/premium")
    public ResponseEntity<Void> cancelarPremium(@PathVariable Long id, Authentication authentication) {
        String mailUsuario = authentication.getName();
        usuarioService.cancelarPremium(id, mailUsuario);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener generación de planes restantes del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Usos de IA para planes restantes")
    @GetMapping("/usos/planes")
    public ResponseEntity<UsosRestantesResponse> obtenerUsosPlanesRestantes(Authentication authentication) {
        String email = authentication.getName();
        UsosRestantesResponse response = usuarioService.obtenerUsosPlanesRestantes(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @Operation(summary = "Obtener generación de menus restantes del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Usos de IA para menus restantes")
    @GetMapping("/usos/menus")
    public ResponseEntity<UsosRestantesResponse> obtenerUsosMenusRestantes(Authentication authentication) {
        String email = authentication.getName();
        UsosRestantesResponse response = usuarioService.obtenerUsosMenusRestantes(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}