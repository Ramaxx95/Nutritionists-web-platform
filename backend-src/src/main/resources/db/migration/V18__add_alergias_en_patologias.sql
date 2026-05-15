-- Agregar tipos de alergias en patologías
UPDATE patologias
SET nombre = 'Alergia a otros alimentos'
WHERE nombre = 'Alergia';

INSERT INTO patologias (nombre) VALUES 
('Alergia al pescado'),
('Alergia a la nuez'),
('Alergia al mani'),
('Alergia al huevo'),
('Alergia a la soja'),
('Alergia al sésamo'),
('Alergia al tomate'),
('Alergia a los cereales con gluten');

-- Agregar criterios de alergias
INSERT INTO criterios (descripcion, tipo_criterio, flag_objetivo, limite, comparacion, unidad, critico) VALUES
('Que el paciente no consuma ningún tipo de pescado', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma nada que contenga nuez', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma nada que contenga maní', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma huevos de gallina', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma nada de soja', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma nada de sésamo', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma nada que contenga tomate', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma cereales con gluten', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE);

-- Alergia al pescado
INSERT INTO patologias_criterios VALUES (14, 13);

-- Alergia a la nuez
INSERT INTO patologias_criterios VALUES (15, 14);

-- Alergia al mani
INSERT INTO patologias_criterios VALUES (16, 15);

-- Alergia al huevo
INSERT INTO patologias_criterios VALUES (17, 16);

-- Alergia a la soja
INSERT INTO patologias_criterios VALUES (18, 17);

-- Alergia al sésamo
INSERT INTO patologias_criterios VALUES (19, 18);

-- Alergia al tomate
INSERT INTO patologias_criterios VALUES (20, 19);

-- Alergia a los cereales con gluten
INSERT INTO patologias_criterios VALUES (21, 20), (21, 12);