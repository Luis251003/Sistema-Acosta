CREATE TABLE IF NOT EXISTS tb_camiones (
    id_camion BIGINT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(10) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    capacidad DECIMAL(10, 2) NOT NULL,
    numero_tuce VARCHAR(30) NOT NULL,
    tipo_vehiculo VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'INHABILITADO',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_camion),
    CONSTRAINT uk_tb_camiones_placa UNIQUE (placa),
    CONSTRAINT uk_tb_camiones_numero_tuce UNIQUE (numero_tuce),
    CONSTRAINT ck_tb_camiones_capacidad CHECK (capacidad > 0),
    CONSTRAINT ck_tb_camiones_tipo_vehiculo
        CHECK (tipo_vehiculo IN ('INTERNO', 'TERCERIZADO')),
    CONSTRAINT ck_tb_camiones_estado
        CHECK (estado IN ('HABILITADO', 'INHABILITADO')
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
