INSERT INTO tb_tipo_documento_conductor (nombre)
SELECT 'Documento de identidad'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_conductor
    WHERE nombre = 'Documento de identidad'
);

INSERT INTO tb_tipo_documento_conductor (nombre)
SELECT 'Carne de conducir'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_conductor
    WHERE nombre = 'Carne de conducir'
);

INSERT INTO tb_tipo_documento_conductor (nombre)
SELECT 'Certificado medico'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_conductor
    WHERE nombre = 'Certificado medico'
);

INSERT INTO tb_tipo_documento_conductor (nombre)
SELECT 'Antecedentes Policiales'
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_tipo_documento_conductor
    WHERE nombre = 'Antecedentes Policiales'
);
