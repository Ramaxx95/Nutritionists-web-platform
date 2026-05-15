-- Tabla análisis de laboratorio
CREATE TABLE analisis_laboratorio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    notas LONGTEXT,
    UNIQUE (paciente_id, fecha),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);

-- Catálogo de biomarcadores
CREATE TABLE biomarcadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    unidad VARCHAR(20),
    categoria VARCHAR(50)
);

INSERT INTO biomarcadores (nombre, unidad, categoria) VALUES
('Recuento de glóbulos rojos (Eritrocitos)', 'millones / mm3', 'Hemograma'),
('Hematocrito', '%', 'Hemograma'),
('Hemoglobina', 'g/dL', 'Hemograma'),
('Recuento de glóbulos blancos (Leucocitos)', '/mm3', 'Hemograma'),
('HCM (Hemoglobina corpuscular media)', 'pg por célula', 'Hemograma'),
('VCM (Volumen corpuscular medio)', 'fL', 'Hemograma'),
('RDW (Ancho de Distribución Eritrocitaria)', '%', 'Hemograma'),
('Glucemia', 'mg/dL', 'Química'),
('HbA1c (Hemoglobina glicosilada)', '%', 'Química'),
('Uremia (urea)', 'mg/dL', 'Química'),
('Creatinina', 'mg/dL', 'Química'),
('Uricemia (ácido úrico)', 'mg/dL', 'Química'),
('Colesterol total', 'mg/dL', 'Química'),
('HDL colesterol', 'mg/dL', 'Química'),
('LDL colesterol', 'mg/dL', 'Química'),
('Triglicéridos', 'mg/dL', 'Química'),
('Ferremia (Hierro)', 'µg/dL', 'Perfil de Hierro'),
('Transferrina', 'µg/dL', 'Perfil de Hierro'),
('Ferritina', 'ng/mL', 'Perfil de Hierro'),
('Transaminasa (TGO)', 'UI/L', 'Hepatograma'),
('Transaminasa (TGP)', 'UI/L', 'Hepatograma'),
('Proteínas totales', 'g/dL', 'Hepatograma'),
('Albúmina', 'g/dL', 'Hepatograma'),
('Tirotrofina plasmática (TSH)', 'mUI/L', 'Perfil Tiroideo'),
('Triyodotironina libre (T3 libre)', 'pg/mL', 'Perfil Tiroideo'),
('Tiroxina libre (T4 libre)', 'ng/dL', 'Perfil Tiroideo'),
('Vitamina B12', 'pg/mL', 'Vitaminas'),
('Homocisteína', 'µmol/L', 'Vitaminas'),
('Vitamina D', 'ng/mL', 'Vitaminas');

-- Resultados individuales por biomarcador
CREATE TABLE resultados_analisis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    analisis_id BIGINT NOT NULL,
    biomarcador_id INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (analisis_id) REFERENCES analisis_laboratorio(id) ON DELETE CASCADE,
    FOREIGN KEY (biomarcador_id) REFERENCES biomarcadores(id) ON DELETE CASCADE
);

-- Rango de referencia por biomarcador
CREATE TABLE rangos_referencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    biomarcador_id INT NOT NULL,
    descripcion VARCHAR(100),
    sexo ENUM('M', 'F', 'AMBOS') DEFAULT 'AMBOS',
    valor_min DECIMAL(10,2),
    valor_max DECIMAL(10,2),
    FOREIGN KEY (biomarcador_id) REFERENCES biomarcadores(id) ON DELETE CASCADE
);

INSERT INTO rangos_referencia (biomarcador_id, descripcion, sexo, valor_min, valor_max) VALUES
(1, 'Normal', 'M', 4.5, 5.9),
(1, 'Normal', 'F', 4.0, 5.2),
(2, 'Normal', 'M', 41, 53),
(2, 'Normal', 'F', 36, 46),
(3, 'Normal', 'M', 14, 17),
(3, 'Normal', 'F', 12, 16),
(4, 'Normal', 'AMBOS', 4500, 11000),
(5, 'Normal', 'AMBOS', 27, 35),
(6, 'Normal', 'AMBOS', 88, 100),
(7, 'Normal', 'AMBOS', 11, 15),
(8, 'Normal', 'AMBOS', 70, 109.99),
(8, 'Prediabetes', 'AMBOS', 110, 125),
(9, 'Normal', 'AMBOS', NULL, 5.70),
(9, 'Prediabetes', 'AMBOS', 5.71, 6.4),
(10, 'Normal', 'AMBOS', 10, 50),
(11, 'Normal', 'M', 0.5, 1.3),
(11, 'Normal', 'F', 0.5, 1.1),
(12, 'Normal', 'M', 3.4, 7.0),
(12, 'Normal', 'F', 2.4, 6.0),
(13, 'Deseable', 'AMBOS', NULL, 200),
(13, 'Alto', 'AMBOS', 240, NULL),
(14, 'Deseable', 'M', 50, NULL),
(14, 'Deseable', 'F', 60, NULL),
(15, 'Óptimo', 'AMBOS', NULL, 99.99),
(15, 'Cercano óptimo', 'AMBOS', 100.00, 129.99),
(15, 'Límite', 'AMBOS', 130, 160),
(15, 'Alto', 'AMBOS', 160.01, NULL),
(16, 'Deseable', 'AMBOS', NULL, 149.99),
(16, 'Límite', 'AMBOS', 150, 200),
(16, 'Alto', 'AMBOS', 200.01, NULL),
(17, 'Normal', 'AMBOS', 65, 165),
(18, 'Normal', 'AMBOS', 200, 400),
(19, 'Normal', 'M', 35, 270),
(19, 'Normal', 'F', 30, 160),
(20, 'Normal', 'AMBOS', NULL, 40.99),
(21, 'Normal', 'AMBOS', NULL, 52.99),
(22, 'Normal', 'AMBOS', 6.0, 8.4),
(23, 'Normal', 'AMBOS', 3.5, 5.4),
(24, 'Normal', 'AMBOS', 0.27, 4.20),
(25, 'Normal', 'AMBOS', 2.3, 4.1),
(26, 'Normal', 'AMBOS', 0.8, 2.0),
(27, 'Normal', 'AMBOS', 400, 900),
(28, 'Normal', 'AMBOS', 5, 9),
(29, 'Deficiencia', 'AMBOS', NULL, 19.99),
(29, 'Insuficiencia', 'AMBOS', 20, 29.99),
(29, 'Óptimo', 'AMBOS', 30, 60),
(29, 'Alto', 'AMBOS', 60.01, 150.00),
(29, 'Toxicidad', 'AMBOS', 150.01, NULL);