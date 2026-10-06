--V1__USUARIOS

-- ==========================================
-- INSERTAR ROLES
-- ==========================================
INSERT INTO tb_roles(nombre) VALUES ('ADMIN') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_roles(nombre) VALUES ('OPERADOR') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_roles(nombre) VALUES ('CONDUCTOR') ON DUPLICATE KEY UPDATE nombre = nombre;

-- ==========================================
-- INSERTAR CONDICION DE CLIENTE
-- ==========================================
INSERT INTO tb_condicion_cliente (nombre) VALUES ('HABIDO') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_condicion_cliente (nombre) VALUES ('NO HABIDO') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_condicion_cliente (nombre) VALUES ('NO HALLADO') ON DUPLICATE KEY UPDATE nombre = nombre;

-- ==========================================
-- INSERTAR ESTADO DE CLIENTE
-- ==========================================
INSERT INTO tb_estado_cliente (nombre) VALUES ('ACTIVO') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_estado_cliente (nombre) VALUES ('SUSPENSIÓN TEMPORAL') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA PROVISIONAL') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA DEFINITIVA') ON DUPLICATE KEY UPDATE nombre = nombre;
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA DE OFICIO') ON DUPLICATE KEY UPDATE nombre = nombre;