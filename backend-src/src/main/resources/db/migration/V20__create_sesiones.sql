-- Tabla de sesiones
CREATE TABLE sesiones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    notas TEXT,
    estado VARCHAR(20) NOT NULL,

    CONSTRAINT fk_sesiones_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);