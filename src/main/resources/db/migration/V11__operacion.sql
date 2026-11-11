-- PROPOSITO: CREAR ENTIDAD OPERACION
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

-- CREAMOS LAS TABLAS PARA MANEJAR EL CODIGO DE OPERACION
CREATE TABLE IF NOT EXISTS tb_codigos_operacion (
    id_codigo_operacion BIGINT NOT NULL AUTO_INCREMENT,
    codigo CHAR(14) NOT NULL,
    id_usuario BIGINT NOT NULL,
    estado VARCHAR(10) NOT NULL,

    PRIMARY KEY(id_codigo_operacion),
    CONSTRAINT uk_tb_operaciones_codigo UNIQUE (codigo),
    CONSTRAINT fk_tb_codigos_operacion_usuario
    FOREIGN KEY (id_usuario)
    REFERENCES tb_usuarios (id_usuario)
);

CREATE TABLE IF NOT EXISTS tb_secuencia_codigos (
    anio INT NOT NULL,
    ultimo INT NOT NULL DEFAULT 0,
    PRIMARY KEY(anio)
);

CREATE TABLE IF NOT EXISTS tb_operaciones (
    id_operacion BIGINT NOT NULL AUTO_INCREMENT,
    id_codigo_operacion BIGINT NOT NULL,
    fecha_recojo_programado DATE NOT NULL,
    fecha_entrega_programada DATE NULL,
    hora_entrega_programada TIME(6) NULL,
    token_seguimiento CHAR(36) NOT NULL,
    metodo_resolucion VARCHAR(20) NOT NULL,
    observacion VARCHAR(300) NULL,
    estado VARCHAR(30) NOT NULL,
    activo BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    id_cliente BIGINT NOT NULL,
    id_punto_origen BIGINT NULL,
    id_partida BIGINT NOT NULL,
    id_destino BIGINT NOT NULL,


    PRIMARY KEY(id_operacion),
    CONSTRAINT fk_tb_operaciones_codigo_operacion
    FOREIGN KEY (id_codigo_operacion)
    REFERENCES tb_codigos_operacion (id_codigo_operacion),

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