-- Borro entradas en tabla
DELETE FROM menus WHERE plan_id = 36;

-- Hago unica la foreign key
ALTER TABLE menus
MODIFY plan_id BIGINT UNIQUE NOT NULL;