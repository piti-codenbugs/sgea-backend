ALTER TABLE programa_curso
    DROP CONSTRAINT IF EXISTS fk_programa_curso_curso;

DROP INDEX IF EXISTS idx_programa_curso_codigo_curso;
DROP INDEX IF EXISTS uq_programa_curso_docente_periodo;
DROP INDEX IF EXISTS uq_programa_curso_publico_periodo;

ALTER TABLE programa_curso
    ADD COLUMN codigo_curso_origen VARCHAR(30);

UPDATE programa_curso
SET codigo_curso_origen = codigo_curso::VARCHAR
WHERE codigo_curso_origen IS NULL;

ALTER TABLE programa_curso
    ALTER COLUMN codigo_curso_origen SET NOT NULL;

ALTER TABLE programa_curso
    DROP COLUMN codigo_curso;

CREATE INDEX idx_programa_curso_codigo_origen
    ON programa_curso(codigo_curso_origen);

CREATE UNIQUE INDEX uq_programa_curso_docente_periodo
    ON programa_curso (id_docente, codigo_curso_origen, anio, semestre, seccion)
    WHERE id_docente IS NOT NULL;

CREATE UNIQUE INDEX uq_programa_curso_publico_periodo
    ON programa_curso (codigo_curso_origen, anio, semestre, seccion)
    WHERE id_docente IS NULL;
