-- Eliminar columna patologias que ya no se va a usar
ALTER TABLE pacientes
DROP COLUMN patologias;

-- Crear la tabla intermedia para la relación muchos a muchos
CREATE TABLE pacientes_patologias (
    paciente_id BIGINT NOT NULL,
    patologia_id INT NOT NULL,
    PRIMARY KEY (paciente_id, patologia_id),
    CONSTRAINT fk_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    CONSTRAINT fk_patologia FOREIGN KEY (patologia_id) REFERENCES patologias(id) ON DELETE CASCADE
);