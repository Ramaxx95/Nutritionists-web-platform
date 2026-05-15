-- Eliminar la FK anterior en pacientes
ALTER TABLE pacientes
    DROP FOREIGN KEY fk_usuario_paciente;

-- Eliminar la PK existente en usuarios
ALTER TABLE usuarios
    DROP PRIMARY KEY;

-- Agregar la nueva columna id en usuarios
ALTER TABLE usuarios
    ADD COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

-- Crear la nueva columna de referencia en pacientes
ALTER TABLE pacientes
    ADD COLUMN usuario_id BIGINT;

-- Actualizar la nueva columna usuario_id con el id correcto
UPDATE pacientes p
JOIN usuarios u ON p.email_usuario = u.email
SET p.usuario_id = u.id;

-- Crear la nueva FK en pacientes
ALTER TABLE pacientes
    ADD CONSTRAINT fk_paciente_usuario_id FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE;

-- Eliminar la columna vieja de pacientes
ALTER TABLE pacientes
    DROP COLUMN email_usuario;

-- Asegurar unicidad de emails en usuarios
ALTER TABLE usuarios
    ADD CONSTRAINT uq_email UNIQUE (email);