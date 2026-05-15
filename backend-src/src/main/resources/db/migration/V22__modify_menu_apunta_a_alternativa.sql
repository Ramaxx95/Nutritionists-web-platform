-- Borro la entry cuyo plan posee mas de una alternativa
DELETE FROM menus WHERE plan_id = 44;

-- Agrego la columna que se asociara con una alternativa
ALTER TABLE menus
ADD COLUMN alternativa_id BIGINT NOT NULL;

-- Agrego la columna que se asociara con una alternativa
UPDATE menus m
JOIN alternativas_plan a ON m.plan_id = a.plan_id
SET m.alternativa_id = a.id;

-- Quito la FK anterior
ALTER TABLE menus DROP FOREIGN KEY menus_ibfk_1;

-- Borro la columna de la FK anterior
ALTER TABLE menus DROP COLUMN plan_id;

-- Agrego la nueva FK
ALTER TABLE menus
ADD CONSTRAINT fk_menus_alternativa
FOREIGN KEY (alternativa_id) REFERENCES alternativas_plan(id) ON DELETE CASCADE;