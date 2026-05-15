-- Tabla de patologias
CREATE TABLE patologias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO patologias (nombre) VALUES 
('Hipertensión'),
('Enfermedad cardiaca'),
('Diabetes'),
('Hipercolesterolemia'),
('Hipertrigliceridemia'),
('Hígado graso (Esteatosis hepática no alcohólica)');

-- Tabla de criterios
CREATE TABLE criterios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT NOT NULL,
    nutriente VARCHAR(100) NOT NULL,
    limite DECIMAL(10,2) NOT NULL,
    comparacion ENUM('LT', 'LTE', 'EQ', 'GTE', 'GT') NOT NULL,
    unidad VARCHAR(50) NOT NULL,
    critico BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO criterios (descripcion, nutriente, limite, comparacion, unidad, critico) VALUES
('< 1,5 g de sodio por día', 'Sodio', 1.5, 'LT', 'g/dia', FALSE),
('< 20 g de azúcar agregado por día', 'Azúcar agregado', 20, 'LT', 'g/dia', FALSE),
('> 12 g de fibra alimentaria por día', 'Fibra alimentaria', 12, 'GT', 'g/dia', FALSE),
('< 200 g de colesterol por día', 'Colesterol', 200, 'LT', 'g/dia', FALSE);

-- Tabla de relación entre patologias y criterios
CREATE TABLE patologias_criterios (
    patologia_id INT NOT NULL,
    criterio_id INT NOT NULL,
    PRIMARY KEY (patologia_id, criterio_id),
    FOREIGN KEY (patologia_id) REFERENCES patologias(id) ON DELETE CASCADE,
    FOREIGN KEY (criterio_id) REFERENCES criterios(id) ON DELETE CASCADE
);

-- Hipertensión
INSERT INTO patologias_criterios VALUES (1, 1);

-- Enfermedad cardiaca
INSERT INTO patologias_criterios VALUES (2, 1);

-- Diabetes
INSERT INTO patologias_criterios VALUES (3, 2), (3, 3);

-- Hipercolesterolemia
INSERT INTO patologias_criterios VALUES (4, 4);

-- Hipertrigliceridemia
INSERT INTO patologias_criterios VALUES (5, 2), (5, 3), (5, 4);

-- Hígado graso
INSERT INTO patologias_criterios VALUES (6, 2), (6, 3), (6, 4);