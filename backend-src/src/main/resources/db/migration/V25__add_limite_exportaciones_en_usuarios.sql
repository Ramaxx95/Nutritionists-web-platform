-- Agregar columna exportaciones_restantes con valor por defecto 5
ALTER TABLE usuarios
ADD COLUMN exportaciones_restantes INT NOT NULL DEFAULT 5;

-- Agregar columna ultima_actualizacion_exportaciones con valor por defecto el momento actual
ALTER TABLE usuarios
ADD COLUMN ultima_actualizacion_exportaciones TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;