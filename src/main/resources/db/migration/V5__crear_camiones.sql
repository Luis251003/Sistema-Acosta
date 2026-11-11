-- PROPOSITO: INSERTAR TIPOS DE DOCUMENTOS CONDUCTOR
-- FECHA: 07 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_camiones (
    id_camion BIGINT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    capacidad DECIMAL(10, 2) NOT NULL,
    numero_tuce VARCHAR(30) NOT NULL,
    tipo_vehiculo VARCHAR(20) NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_camion),
    CONSTRAINT uk_tb_camiones_placa UNIQUE (placa),
    CONSTRAINT uk_tb_camiones_numero_tuce UNIQUE (numero_tuce),
    CONSTRAINT ck_tb_camiones_capacidad CHECK (capacidad > 0),
    CONSTRAINT ck_tb_camiones_tipo_vehiculo
        CHECK (tipo_vehiculo IN ('INTERNO', 'TERCERIZADO'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_documentos_camion (
    id_documento BIGINT NOT NULL AUTO_INCREMENT,
    nombre_archivo VARCHAR(100) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_tipo_documento BIGINT NOT NULL,
    id_camion BIGINT NOT NULL,

    PRIMARY KEY (id_documento),
    CONSTRAINT fk_tb_documentos_camion
    FOREIGN KEY (id_camion)
    REFERENCES tb_camiones (id_camion),
    CONSTRAINT fk_tb_documentos_camion_tipo
    FOREIGN KEY (id_tipo_documento)
    REFERENCES tb_tipo_documento (id_tipo_documento)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;