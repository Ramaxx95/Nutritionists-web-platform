-- Eliminar el índice UNIQUE existente sobre 'nombre' en la tabla alimentos
ALTER TABLE alimentos DROP INDEX nombre;

-- Agregar restricción UNIQUE compuesta sobre (usuario_id, nombre)
ALTER TABLE alimentos ADD CONSTRAINT unique_usuario_nombre UNIQUE (usuario_id, nombre);