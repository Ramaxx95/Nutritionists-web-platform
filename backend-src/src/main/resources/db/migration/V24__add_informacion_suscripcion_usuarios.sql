-- Agregar columna tipo_subscripcion
ALTER TABLE usuarios
    ADD COLUMN tipo_subscripcion VARCHAR(20) NOT NULL DEFAULT 'DEMO';

-- Agregar columna fecha_registro con valor por defecto actual
ALTER TABLE usuarios
    ADD COLUMN fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Agregar columna fecha_ultimo_pago (nullable)
ALTER TABLE usuarios
    ADD COLUMN fecha_ultimo_pago DATETIME NULL;