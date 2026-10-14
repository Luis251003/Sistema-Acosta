CREATE TABLE IF NOT EXISTS tb_remitentes (
    id_remitente BIGINT NOT NULL AUTO_INCREMENT,
    ruc VARCHAR(11) NOT NULL,
    razon_social VARCHAR(200) NOT NULL,
    PRIMARY KEY (id_remitente),
    CONSTRAINT uk_tb_remitentes_ruc UNIQUE (ruc),
    CONSTRAINT ck_tb_remitentes_ruc CHECK (ruc REGEXP '^[0-9]{11}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_operaciones (
    id_operacion BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(20),
    id_remitente BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    peso DECIMAL(10, 2) NOT NULL,
    fecha_viaje DATETIME(6) NOT NULL,
    id_partida BIGINT NOT NULL,
    id_destino BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_operacion),
    CONSTRAINT uk_tb_operaciones_codigo UNIQUE (codigo),
    CONSTRAINT ck_tb_operaciones_peso CHECK (peso > 0),
    CONSTRAINT fk_tb_operaciones_remitente
        FOREIGN KEY (id_remitente) REFERENCES tb_remitentes (id_remitente),
    CONSTRAINT fk_tb_operaciones_cliente
        FOREIGN KEY (id_cliente) REFERENCES tb_clientes (id_cliente),
    CONSTRAINT fk_tb_operaciones_partida
        FOREIGN KEY (id_partida) REFERENCES tb_ubicaciones (id_ubicacion),
    CONSTRAINT fk_tb_operaciones_destino
        FOREIGN KEY (id_destino) REFERENCES tb_ubicaciones (id_ubicacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_flota (
    id_flota BIGINT NOT NULL AUTO_INCREMENT,
    id_operacion BIGINT NOT NULL,
    id_conductor BIGINT NOT NULL,
    rol VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_flota),
    CONSTRAINT uk_tb_flota_operacion_conductor UNIQUE (id_operacion, id_conductor),
    CONSTRAINT ck_tb_flota_rol CHECK (rol IN ('PRINCIPAL', 'SECUNDARIO')),
    CONSTRAINT fk_tb_flota_operacion
        FOREIGN KEY (id_operacion) REFERENCES tb_operaciones (id_operacion),
    CONSTRAINT fk_tb_flota_conductor
        FOREIGN KEY (id_conductor) REFERENCES tb_conductores (id_conductor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
