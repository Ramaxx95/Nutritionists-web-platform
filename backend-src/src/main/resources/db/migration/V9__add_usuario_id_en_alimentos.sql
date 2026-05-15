-- Agregar columna usuario_id a alimento
ALTER TABLE alimentos ADD COLUMN usuario_id BIGINT NULL;

-- Crear FK a la tabla usuario
ALTER TABLE alimentos
ADD CONSTRAINT fk_alimento_usuario
FOREIGN KEY (usuario_id)
REFERENCES usuarios(id);