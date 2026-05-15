-- Agregar columna ecuacion
ALTER TABLE planes
ADD COLUMN ecuacion ENUM('ADA', 'HARRIS_BENEDICT', 'FAO_OMS') NOT NULL DEFAULT 'ADA';

-- Agregar columna distribucion
ALTER TABLE planes
ADD COLUMN distribucion ENUM('TRADICIONAL', 'PROTEICA') NOT NULL DEFAULT 'TRADICIONAL';

-- Agregar columna kcal_objetivo
ALTER TABLE planes
ADD COLUMN kcal_objetivo DECIMAL(7,2) DEFAULT NULL;

-- Agregar columna carbohidratos_objetivo
ALTER TABLE planes
ADD COLUMN carbohidratos_objetivo DECIMAL(7,2) DEFAULT NULL;

-- Agregar columna proteinas_objetivo
ALTER TABLE planes
ADD COLUMN proteinas_objetivo DECIMAL(7,2) DEFAULT NULL;

-- Agregar columna grasas_objetivo
ALTER TABLE planes
ADD COLUMN grasas_objetivo DECIMAL(7,2) DEFAULT NULL;