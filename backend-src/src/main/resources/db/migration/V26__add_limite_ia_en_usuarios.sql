-- Agregar campos de contadores IA y ultima actualizacion global
ALTER TABLE usuarios
    ADD COLUMN usos_plan_ia_restantes INT DEFAULT 20,
    ADD COLUMN usos_menu_ia_restantes INT DEFAULT 20,
    ADD COLUMN ultima_actualizacion_contadores DATETIME DEFAULT CURRENT_TIMESTAMP;

-- Eliminar campo de ultima actualizacion individual
ALTER TABLE usuarios
    DROP COLUMN ultima_actualizacion_exportaciones;