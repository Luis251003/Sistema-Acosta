CREATE TABLE IF NOT EXISTS tb_persona (
    id_persona BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    dni VARCHAR(8) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_persona),
    CONSTRAINT uk_tb_persona_telefono UNIQUE (telefono),
    CONSTRAINT uk_tb_persona_dni UNIQUE (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_conductores (
    id_conductor BIGINT NOT NULL AUTO_INCREMENT,
    brevete VARCHAR(10) NOT NULL,
    foto_url VARCHAR(500),
    tipo_conductor VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    id_persona BIGINT NOT NULL,
    PRIMARY KEY (id_conductor),
    CONSTRAINT uk_tb_conductores_brevete UNIQUE (brevete),
    CONSTRAINT uk_tb_conductores_persona UNIQUE (id_persona),
    CONSTRAINT ck_tb_conductores_tipo CHECK (tipo_conductor IN ('INTERNO', 'TERCERIZADO')),
    CONSTRAINT fk_tb_conductores_persona
        FOREIGN KEY (id_persona) REFERENCES tb_persona (id_persona)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET @has_tipo_conductor_column = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'tb_conductores'
      AND column_name = 'tipo_conductor'
);
SET @migration_sql = IF(
    @has_tipo_conductor_column = 0,
    'ALTER TABLE tb_conductores ADD COLUMN tipo_conductor VARCHAR(20) NULL',
    'SELECT 1'
);
PREPARE migration_statement FROM @migration_sql;
EXECUTE migration_statement;
DEALLOCATE PREPARE migration_statement;

SET @has_legacy_tipo_table = (
    SELECT COUNT(*)
    FROM information_schema.tables
    WHERE table_schema = DATABASE()
      AND table_name = 'tb_tipo_conductor'
);
SET @has_legacy_tipo_id_column = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'tb_conductores'
      AND column_name = 'id_tipo_conductor'
);
SET @migration_sql = IF(
    @has_legacy_tipo_table > 0 AND @has_legacy_tipo_id_column > 0,
    'UPDATE tb_conductores c
     JOIN tb_tipo_conductor tc ON tc.id_tipo_conductor = c.id_tipo_conductor
     SET c.tipo_conductor = UPPER(TRIM(tc.nombre))
     WHERE c.tipo_conductor IS NULL OR TRIM(c.tipo_conductor) = ''''',
    'SELECT 1'
);
PREPARE migration_statement FROM @migration_sql;
EXECUTE migration_statement;
DEALLOCATE PREPARE migration_statement;

UPDATE tb_conductores
SET tipo_conductor = UPPER(TRIM(tipo_conductor))
WHERE tipo_conductor IS NOT NULL;

ALTER TABLE tb_conductores
    MODIFY COLUMN tipo_conductor VARCHAR(20) NOT NULL;

SET @has_tipo_check = (
    SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE constraint_schema = DATABASE()
      AND table_name = 'tb_conductores'
      AND constraint_name = 'ck_tb_conductores_tipo'
);
SET @migration_sql = IF(
    @has_tipo_check = 0,
    'ALTER TABLE tb_conductores
     ADD CONSTRAINT ck_tb_conductores_tipo
     CHECK (tipo_conductor IN (''INTERNO'', ''TERCERIZADO''))',
    'SELECT 1'
);
PREPARE migration_statement FROM @migration_sql;
EXECUTE migration_statement;
DEALLOCATE PREPARE migration_statement;

CREATE TABLE IF NOT EXISTS tb_tipo_documento_conductor (
    id_tipo_documento_conductor BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_tipo_documento_conductor),
    CONSTRAINT uk_tb_tipo_documento_conductor_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_documentos_conductor (
    id_documento BIGINT NOT NULL AUTO_INCREMENT,
    id_conductor BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    id_tipo_documento_conductor BIGINT NOT NULL,
    ruta_archivo VARCHAR(500) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_documento),
    CONSTRAINT fk_tb_documentos_conductor_conductor
        FOREIGN KEY (id_conductor) REFERENCES tb_conductores (id_conductor),
    CONSTRAINT fk_tb_documentos_conductor_tipo
        FOREIGN KEY (id_tipo_documento_conductor)
        REFERENCES tb_tipo_documento_conductor (id_tipo_documento_conductor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
