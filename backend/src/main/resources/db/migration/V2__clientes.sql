CREATE TABLE IF NOT EXISTS tb_condicion_cliente (
    id_condicion BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_condicion),
    CONSTRAINT uk_tb_condicion_cliente_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_estado_cliente (
    id_estado BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_estado),
    CONSTRAINT uk_tb_estado_cliente_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_clientes (
    id_cliente BIGINT NOT NULL AUTO_INCREMENT,
    ruc VARCHAR(11) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    direccion_fiscal VARCHAR(300),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_estado BIGINT NOT NULL,
    id_condicion BIGINT NOT NULL,
    PRIMARY KEY (id_cliente),
    CONSTRAINT uk_tb_clientes_ruc UNIQUE (ruc),
    CONSTRAINT fk_tb_clientes_estado
        FOREIGN KEY (id_estado) REFERENCES tb_estado_cliente (id_estado),
    CONSTRAINT fk_tb_clientes_condicion
        FOREIGN KEY (id_condicion) REFERENCES tb_condicion_cliente (id_condicion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tb_condicion_cliente (nombre) VALUES ('HABIDO')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_condicion_cliente (nombre) VALUES ('NO HABIDO')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_condicion_cliente (nombre) VALUES ('NO HALLADO')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO tb_estado_cliente (nombre) VALUES ('ACTIVO')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_estado_cliente (nombre) VALUES ('SUSPENSIÓN TEMPORAL')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA PROVISIONAL')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA DEFINITIVA')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_estado_cliente (nombre) VALUES ('BAJA DE OFICIO')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
