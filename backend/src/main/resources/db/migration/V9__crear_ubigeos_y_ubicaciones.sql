CREATE TABLE IF NOT EXISTS tb_ubigeos (
    id_ubigeo VARCHAR(6) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    provincia VARCHAR(100) NOT NULL,
    distrito VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_ubigeo),
    CONSTRAINT uk_tb_ubigeos_departamento_provincia_distrito
        UNIQUE (departamento, provincia, distrito),
    CONSTRAINT ck_tb_ubigeos_id
        CHECK (id_ubigeo REGEXP '^[0-9]{6}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_ubicaciones (
    id_ubicacion BIGINT NOT NULL AUTO_INCREMENT,
    id_ubigeo VARCHAR(6) NOT NULL,
    direccion_detallada VARCHAR(300) NOT NULL,
    latitud DECIMAL(10, 7) NOT NULL,
    longitud DECIMAL(10, 7) NOT NULL,
    PRIMARY KEY (id_ubicacion),
    CONSTRAINT ck_tb_ubicaciones_latitud CHECK (latitud BETWEEN -90 AND 90),
    CONSTRAINT ck_tb_ubicaciones_longitud CHECK (longitud BETWEEN -180 AND 180),
    CONSTRAINT fk_tb_ubicaciones_ubigeo
        FOREIGN KEY (id_ubigeo) REFERENCES tb_ubigeos (id_ubigeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
