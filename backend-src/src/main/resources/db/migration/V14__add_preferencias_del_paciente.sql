-- Borro columna en la tabla de pacientes
ALTER TABLE pacientes DROP COLUMN alimentos_restringidos;

-- Creo la tabla para los alimentos preferidos
CREATE TABLE alimentos_preferidos (
      id INT AUTO_INCREMENT PRIMARY KEY,
      alimento_id INT NOT NULL
);

-- Creo la tabla para los alimentos restringidos
CREATE TABLE alimentos_restringidos (
      id INT AUTO_INCREMENT PRIMARY KEY,
      alimento_id INT NOT NULL
);

-- Creo la tabla de union entre alimentos preferidos y pacientes
CREATE TABLE preferencias_gustos (
      paciente_id BIGINT NOT NULL,
      alimento_preferido_id INT NOT NULL,
      PRIMARY KEY (paciente_id, alimento_preferido_id),
      FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
      FOREIGN KEY (alimento_preferido_id) REFERENCES alimentos_preferidos(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);

-- Creo la tabla de union entre alimentos restringidos y pacientes
CREATE TABLE preferencias_disgustos (
      paciente_id BIGINT NOT NULL,
      alimento_restringido_id INT NOT NULL,
      PRIMARY KEY (paciente_id, alimento_restringido_id),
      FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
      FOREIGN KEY (alimento_restringido_id) REFERENCES alimentos_restringidos(id)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);