-- PROPOSITO: CREAR ENTIDAD OPERACION
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_operaciones (
    id_operacion BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL,
    timestamp_recojo_programado DATETIME(6) NOT NULL,
    timestamp_entrega_programada DATETIME(6) NOT NULL,
    token_seguimiento CHAR(36) NOT NULL,
    observacion VARCHAR(255) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    id_agencia_aduana BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    id_partida BIGINT NOT NULL,
    id_destino BIGINT NOT NULL,
    id_punto_origen BIGINT NOT NULL,


    PRIMARY KEY(id_operacion),
    CONSTRAINT uk_tb_operaciones_codigo UNIQUE (codigo),
    CONSTRAINT fk_tb_operaciones_agencia_aduana
    FOREIGN KEY (id_agencia_aduana)
    REFERENCES tb_agencias_aduanas (id_agencia_aduana),
    CONSTRAINT fk_tb_operaciones_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES tb_clientes (id_cliente),
    CONSTRAINT fk_tb_operaciones_partida
    FOREIGN KEY (id_partida)
    REFERENCES tb_ubicaciones (id_ubicacion),
    CONSTRAINT fk_tb_operaciones_destino
    FOREIGN KEY (id_destino)
    REFERENCES tb_ubicaciones (id_ubicacion),
    CONSTRAINT fk_tb_operaciones_punto_origen
    FOREIGN KEY (id_punto_origen)
    REFERENCES tb_puntos_origen (id_punto_origen)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;