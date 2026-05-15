-- Agregar columna auto_renovacion_activa con valor por defecto falso
ALTER TABLE usuarios ADD COLUMN auto_renovacion_activa BOOLEAN NOT NULL DEFAULT FALSE;