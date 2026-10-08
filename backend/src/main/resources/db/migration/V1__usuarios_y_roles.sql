CREATE TABLE IF NOT EXISTS tb_roles (
    id_rol BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_rol),
    CONSTRAINT uk_tb_roles_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_usuarios (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id_usuario),
    CONSTRAINT uk_tb_usuarios_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tb_usuario_rol (
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    CONSTRAINT fk_tb_usuario_rol_usuario
        FOREIGN KEY (id_usuario) REFERENCES tb_usuarios (id_usuario),
    CONSTRAINT fk_tb_usuario_rol_rol
        FOREIGN KEY (id_rol) REFERENCES tb_roles (id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO tb_roles (nombre) VALUES ('ADMIN')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_roles (nombre) VALUES ('OPERADOR')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
INSERT INTO tb_roles (nombre) VALUES ('CONDUCTOR')
    ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);
