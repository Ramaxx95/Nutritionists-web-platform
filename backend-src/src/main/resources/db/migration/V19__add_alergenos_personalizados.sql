-- Agregar columna de alergenos personalizados para los pacientes
ALTER TABLE pacientes
ADD COLUMN alergenos_personalizados TEXT;