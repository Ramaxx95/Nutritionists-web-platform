-- Tabla de tokens de restablecimiento de contraseña
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    CONSTRAINT fk_usuario_reset FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);