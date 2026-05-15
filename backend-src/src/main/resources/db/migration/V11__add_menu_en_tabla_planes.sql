-- Crear la tabla para la relación uno a uno con 'planes'
CREATE TABLE menus (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      desayuno TEXT,
      almuerzo TEXT,
      merienda TEXT,
      cena TEXT,
      plan_id BIGINT NOT NULL,
      FOREIGN KEY (plan_id) REFERENCES planes(id) ON DELETE CASCADE
);