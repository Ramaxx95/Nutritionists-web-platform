-- Tabla de historial de peso del paciente
CREATE TABLE peso_paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    peso DECIMAL(5,2) NOT NULL,
    sesion_id BIGINT,

    CONSTRAINT fk_peso_paciente_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    CONSTRAINT fk_peso_paciente_sesion FOREIGN KEY (sesion_id) REFERENCES sesiones(id),
    CONSTRAINT uc_paciente_fecha UNIQUE (paciente_id, fecha)
);