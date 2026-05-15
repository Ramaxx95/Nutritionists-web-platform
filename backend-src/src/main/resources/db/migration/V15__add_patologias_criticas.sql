-- Crear tabla de alergias de los pacientes
CREATE TABLE pacientes_alergenos (
    paciente_id BIGINT NOT NULL,
    alimento_id INT NOT NULL,
    PRIMARY KEY (paciente_id, alimento_id),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    FOREIGN KEY (alimento_id) REFERENCES alimentos(id) ON DELETE CASCADE
);

-- Expandir modelo de criterios para distintos tipos
ALTER TABLE criterios
MODIFY nutriente VARCHAR(100) NULL,
ADD COLUMN tipo_criterio ENUM('NUTRIENTE', 'FLAG', 'CATEGORIA', 'ALERGIA') NOT NULL DEFAULT 'NUTRIENTE',
ADD COLUMN flag_objetivo VARCHAR(100);

-- Crear tabla de categorias por criterio
CREATE TABLE criterios_categorias (
    criterio_id INT NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    PRIMARY KEY (criterio_id, categoria),
    FOREIGN KEY (criterio_id) REFERENCES criterios(id) ON DELETE CASCADE
);

-- Agregar patologías faltantes
INSERT INTO patologias (nombre) VALUES 
('Celiaquía'),
('Enfermedad renal'),
('Intolerancia a la lactosa'),
('Hiperuricemia'),
('Alergia'),
('Vegetarianismo'),
('Veganismo');

-- Agregar criterios faltantes
INSERT INTO criterios (descripcion, tipo_criterio, flag_objetivo, limite, comparacion, unidad, critico) VALUES
('Que el paciente no consuma alimentos no aptos para celíacos', 'FLAG', 'apto_celiaco', 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma más de un alimento de categoría “Carnes” o “Pescados y mariscos” por día', 'CATEGORIA', NULL, 1, 'LTE', 'unidades/dia', FALSE),
('Que el paciente no consuma alimentos de las categorías “Leche y postres de leche”, “Yogures” ni “Quesos”', 'CATEGORIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma más de un alimento de categoría “Carnes” por día', 'CATEGORIA', NULL, 1, 'LTE', 'unidades/dia', FALSE),
('Que el paciente no consuma nada del alimento al que es alérgico', 'ALERGIA', NULL, 0, 'EQ', 'unidades/dia', TRUE),
('Que el paciente no consuma alimentos de las categorías “Carnes” ni “Pescados y mariscos”', 'CATEGORIA', NULL, 0, 'EQ', 'unidades/dia', FALSE),
('Que el paciente no consuma alimentos de las categorías “Carnes”, “Pescados y mariscos”, “Leche y postres de leche”, “Yogures”, “Quesos” ni "Huevos"', 'CATEGORIA', NULL, 0, 'EQ', 'unidades/dia', FALSE),
('Que el paciente no consuma alimentos ultraprocesados', 'FLAG', 'ultraprocesado', 0, 'EQ', 'unidades/dia', FALSE);

-- Relacionar categorías con criterios
INSERT INTO criterios_categorias (criterio_id, categoria) VALUES
(6, 'Carnes'),
(6, 'Pescados y mariscos'),
(7, 'Leche y postres de leche'),
(7, 'Yogures'),
(7, 'Quesos'),
(8, 'Carnes'),
(10, 'Carnes'),
(10, 'Pescados y mariscos'),
(11, 'Carnes'),
(11, 'Pescados y mariscos'),
(11, 'Leche y postres de leche'),
(11, 'Yogures'),
(11, 'Quesos'),
(11, 'Huevos');

-- Celiaquía
INSERT INTO patologias_criterios VALUES (7, 5);

-- Enfermedad renal
INSERT INTO patologias_criterios VALUES (8, 1), (8, 6);

-- Intolerancia la lactosa
INSERT INTO patologias_criterios VALUES (9, 7);

-- Hiperuricemia
INSERT INTO patologias_criterios VALUES (10, 8);

-- Alergia
INSERT INTO patologias_criterios VALUES (11, 9);

-- Vegetarianismo
INSERT INTO patologias_criterios VALUES (12, 10);

-- Veganismo
INSERT INTO patologias_criterios VALUES (13, 11), (13, 12);