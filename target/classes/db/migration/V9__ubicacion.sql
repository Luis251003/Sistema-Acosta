-- PROPOSITO: CREAR ENTIDADES PARA MANEJAR LA UBICACION DE LOS PUNTOS DE TRANSPORTE
-- FECHA: 26 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

CREATE TABLE IF NOT EXISTS tb_puntos_origen(
    id_punto_origen BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY(id_punto_origen),
    CONSTRAINT uk_tb_puntos_origen_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_ubigeos(
    cod_ubigeo CHAR(6) NOT NULL,
    cod_departamento CHAR(2) NOT NULL,
    cod_provincia CHAR(2) NOT NULL,
    cod_distrito CHAR(2) NOT NULL,
    departamento VARCHAR(100) NOT NULL,
    provincia VARCHAR(100) NOT NULL,
    distrito VARCHAR(100) NOT NULL,

    PRIMARY KEY(cod_ubigeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_ubicaciones(
    id_ubicacion BIGINT NOT NULL AUTO_INCREMENT,
    direccion VARCHAR(300) NOT NULL,
    latitud DECIMAL(10, 7) NOT NULL,
    longitud DECIMAL(10, 7) NOT NULL,
    cod_ubigeo CHAR(6) NOT NULL,
    PRIMARY KEY(id_ubicacion),
    CONSTRAINT fk_tb_ubicacion_tb_ubigeos
    FOREIGN KEY (cod_ubigeo)
    REFERENCES tb_ubigeos(cod_ubigeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;