-- Tabla de alimentos
CREATE TABLE alimentos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  categoria VARCHAR(100) NOT NULL,
  nombre VARCHAR(255) NOT NULL UNIQUE,
  valor_energetico INT UNSIGNED,
  agua DECIMAL(5,2),
  proteinas DECIMAL(5,2),
  lipidos_totales DECIMAL(5,2),
  colesterol DECIMAL(6,2),
  saturados DECIMAL(5,3),
  monoinsaturados DECIMAL(5,3),
  polininsaturados DECIMAL(5,3),
  trans DECIMAL(5,3),
  cis_linoleico DECIMAL(5,3),
  cis_alfa_linolenico DECIMAL(5,3),
  araquidonico DECIMAL(5,3),
  eicosapentaenoico DECIMAL(5,3),
  docosahexaenoico DECIMAL(5,3),
  carbohidratos_disponibles DECIMAL(5,2),
  carbohidratos_totales DECIMAL(5,2),
  azucar_total DECIMAL(5,2),
  azucar_agregado DECIMAL(5,2),
  fibra_alimentaria DECIMAL(5,2),
  alcohol DECIMAL(5,2),
  cenizas DECIMAL(7,4),
  sodio INT,
  potasio INT,
  calcio INT,
  cobre DECIMAL(6,4),
  fosforo INT,
  hierro DECIMAL(5,2),
  magnesio INT,
  zinc DECIMAL(5,3),
  niacina DECIMAL(5,3),
  folato_efd INT,
  acido_folico INT,
  vitamina_a INT,
  retinol INT,
  tiamina DECIMAL(5,3),
  riboflavina DECIMAL(5,3),
  vitamina_b12 DECIMAL(5,3),
  vitamina_c DECIMAL(6,2),
  vitamina_d DECIMAL(5,2)
)

-- Tabla de usuarios
CREATE TABLE usuarios (
  email VARCHAR(255) NOT NULL PRIMARY KEY,
  password VARCHAR(255) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellido VARCHAR(50) NOT NULL
);

-- Tabla de pacientes
CREATE TABLE pacientes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  apellido VARCHAR(255) NOT NULL,
  sexo VARCHAR(50),
  altura FLOAT,
  peso FLOAT,
  objetivo VARCHAR(255) NOT NULL,
  peso_objetivo FLOAT,
  patologias TEXT,
  alimentos_restringidos TEXT,
  email_usuario VARCHAR(255) NOT NULL,
  fecha_nacimiento DATE,
  CONSTRAINT fk_usuario_paciente FOREIGN KEY (email_usuario) REFERENCES usuarios (email) ON DELETE CASCADE
)

-- Tabla de planes de alimentación
CREATE TABLE planes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    objetivo VARCHAR(255),
    fecha_inicio DATE,
    fecha_fin DATE,
    activo BOOLEAN DEFAULT TRUE,
    paciente_id BIGINT NOT NULL,
    CONSTRAINT fk_planes_paciente FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE
);

-- Tabla de comidas dentro del plan
CREATE TABLE comidas_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('DESAYUNO', 'ALMUERZO', 'MERIENDA', 'CENA'),
    plan_id BIGINT NOT NULL,
    CONSTRAINT fk_comidas_plan FOREIGN KEY (plan_id) REFERENCES planes(id) ON DELETE CASCADE
);

-- Tabla de detalles de cada comida
CREATE TABLE detalles_comida (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad DOUBLE,
    alimento_id INT NOT NULL,
    comida_id BIGINT NOT NULL,
    CONSTRAINT fk_detalles_alimento FOREIGN KEY (alimento_id) REFERENCES alimentos(id),
    CONSTRAINT fk_detalles_comida FOREIGN KEY (comida_id) REFERENCES comidas_plan(id) ON DELETE CASCADE
);