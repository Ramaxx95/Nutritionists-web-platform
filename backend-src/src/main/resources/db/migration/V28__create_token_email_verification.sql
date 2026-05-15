-- Agregar campo de email verificado
ALTER TABLE usuarios ADD COLUMN email_verificado BOOLEAN NOT NULL DEFAULT FALSE;

-- Tabla de tokens de verificación de correo
CREATE TABLE email_verification_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    CONSTRAINT fk_usuario_email_verificacion FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);