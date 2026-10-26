-- PROPOSITO: INSERTAR TIPOS DE DOCUMENTOS CONDUCTOR
-- FECHA: 06 DE OCTUBRE DE 2026
-- AUTOR: LUIS CABRERA

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Documento de identidad', 'CONDUCTOR'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Documento de identidad'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Carne de conducir','CONDUCTOR'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Carne de conducir'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Certificado medico', 'CONDUCTOR'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Certificado medico'
);

INSERT INTO tb_tipo_documento (nombre, rol)
SELECT 'Antecedentes Policiales', 'CONDUCTOR'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento
    WHERE nombre = 'Antecedentes Policiales'
);
