CREATE TABLE IF NOT EXISTS tb_tipo_documento_camion (
    id_tipo_documento_camion BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_tipo_documento_camion),
    CONSTRAINT uk_tb_tipo_documento_camion_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_documentos_camion (
    id_documento_camion BIGINT NOT NULL AUTO_INCREMENT,
    id_camion BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    id_tipo_documento_camion BIGINT NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_documento_camion),
    CONSTRAINT ck_tb_documentos_camion_estado
        CHECK (estado IN ('VENCIDO', 'POR VENCER', 'VIGENTE')),
    CONSTRAINT ck_tb_documentos_camion_fechas
        CHECK (fecha_vencimiento >= fecha_emision),
    CONSTRAINT uk_tb_documentos_camion_tipo
        UNIQUE (id_camion, id_tipo_documento_camion),
    CONSTRAINT fk_tb_documentos_camion_camion
        FOREIGN KEY (id_camion) REFERENCES tb_camiones (id_camion),
    CONSTRAINT fk_tb_documentos_camion_tipo
        FOREIGN KEY (id_tipo_documento_camion)
        REFERENCES tb_tipo_documento_camion (id_tipo_documento_camion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
