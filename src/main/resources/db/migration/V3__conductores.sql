-- PROPOSITO: CREAR LA ENTIDAD CONDUCTOR
-- FECHA: 06 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_conductores (
    id_conductor BIGINT NOT NULL AUTO_INCREMENT,
    brevete VARCHAR(10) NOT NULL,
    foto_url VARCHAR(500),
    tipo_conductor VARCHAR(20) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT FALSE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_empleado BIGINT NOT NULL,

    PRIMARY KEY (id_conductor),
    CONSTRAINT uk_tb_conductores_brevete UNIQUE (brevete),
    CONSTRAINT uk_tb_conductores_empleado UNIQUE (id_empleado),
    CONSTRAINT ck_tb_conductores_tipo
    CHECK (tipo_conductor IN ('INTERNO', 'TERCERIZADO')),
    CONSTRAINT fk_tb_conductores_empleado
    FOREIGN KEY (id_empleado)
    REFERENCES tb_empleados (id_empleado)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS tb_tipo_documento (
    id_tipo_documento BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_tipo_documento),
    CONSTRAINT uk_tb_tipo_documento_nombre UNIQUE (nombre)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS tb_documentos_conductor (
    id_documento BIGINT NOT NULL AUTO_INCREMENT,
    nombre_archivo VARCHAR(100) NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_tipo_documento BIGINT NOT NULL,
    id_conductor BIGINT NOT NULL,

    PRIMARY KEY (id_documento),
    CONSTRAINT fk_tb_documentos_conductor_conductor
    FOREIGN KEY (id_conductor)
    REFERENCES tb_conductores (id_conductor),
    CONSTRAINT fk_tb_documentos_conductor_tipo
    FOREIGN KEY (id_tipo_documento)
    REFERENCES tb_tipo_documento (id_tipo_documento)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
