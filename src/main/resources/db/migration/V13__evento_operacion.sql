-- PROPOSITO: CREAR ENTIDAD EVENTO OPERACION
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_eventos_operacion(
    id_evento_operacion BIGINT NOT NULL AUTO_INCREMENT,
    tipo_evento VARCHAR(20) NOT NULL,
    timestamp DATETIME(6) NOT NULL,
    observacion VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,

    id_operacion BIGINT NOT NULL,

    PRIMARY KEY (id_evento_operacion),
    CONSTRAINT fk_tb_eventos_operacion_operacion
    FOREIGN KEY (id_operacion)
    REFERENCES tb_operaciones (id_operacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;