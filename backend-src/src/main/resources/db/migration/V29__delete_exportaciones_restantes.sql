-- Eliminar columna con usos restantes para exportar PDFs
ALTER TABLE usuarios
    DROP COLUMN exportaciones_restantes;