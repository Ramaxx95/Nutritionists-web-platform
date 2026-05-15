-- Creo la nueva tabla alternativas
CREATE TABLE alternativas_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    FOREIGN KEY (plan_id) REFERENCES planes(id) ON DELETE CASCADE
);

-- Creo una alternativa por cada plan existente
INSERT INTO alternativas_plan (plan_id)
SELECT id FROM planes;

-- Agrego columna alternativa_id a comidas
ALTER TABLE comidas_plan
ADD COLUMN alternativa_id BIGINT NOT NULL;

-- Paso comidas.plan_id a alternativas.id
UPDATE comidas_plan c
JOIN  alternativas_plan a ON c.plan_id = a.plan_id
SET c.alternativa_id = a.id;

-- Quito la fk vieja de plan_id
ALTER TABLE comidas_plan DROP CONSTRAINT fk_comidas_plan;

-- Borro la columna plan_id
ALTER TABLE comidas_plan DROP COLUMN plan_id;

-- Agrego la nueva fk que apunte a alternativas
ALTER TABLE comidas_plan
ADD CONSTRAINT fk_comidas_alternativa
FOREIGN KEY (alternativa_id) REFERENCES alternativas_plan(id) ON DELETE CASCADE;