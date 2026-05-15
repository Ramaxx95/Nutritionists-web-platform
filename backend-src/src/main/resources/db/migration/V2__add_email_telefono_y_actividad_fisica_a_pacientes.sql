-- Agregar columna email_paciente
ALTER TABLE pacientes
ADD COLUMN email_paciente VARCHAR(255) DEFAULT NULL;

-- Agregar columna telefono
ALTER TABLE pacientes
ADD COLUMN telefono VARCHAR(255) DEFAULT NULL;

-- Agregar columna actividad_fisica
ALTER TABLE pacientes
ADD COLUMN actividad_fisica ENUM('Muy leve', 'Leve', 'Moderada', 'Intensa') DEFAULT NULL;