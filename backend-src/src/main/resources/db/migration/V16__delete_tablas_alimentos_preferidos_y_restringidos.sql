-- Elimino la foreign key de tabla de 'preferencias_gustos'
ALTER TABLE preferencias_gustos
    DROP FOREIGN KEY preferencias_gustos_ibfk_2;

-- Hago que apunte a la tabla 'alimentos'
ALTER TABLE preferencias_gustos
    ADD FOREIGN KEY (alimento_preferido_id) REFERENCES alimentos(id) ON DELETE CASCADE ON UPDATE CASCADE;

-- Elimino la foreign key de tabla de 'preferencias_disgustos'
ALTER TABLE preferencias_disgustos
    DROP FOREIGN KEY preferencias_disgustos_ibfk_2;

-- Hago que apunte a la tabla 'alimentos'
ALTER TABLE preferencias_disgustos
    ADD FOREIGN KEY (alimento_restringido_id) REFERENCES alimentos(id) ON DELETE CASCADE ON UPDATE CASCADE;

-- Borro la tabla de 'alimentos_preferidos'
DROP TABLE alimentos_preferidos;

-- Borro la tabla de 'alimentos_restringidos'
DROP TABLE alimentos_restringidos;

-- Borro las entries en la tabla 'preferencias_disgustos'
DELETE FROM preferencias_disgustos WHERE paciente_id = 81;