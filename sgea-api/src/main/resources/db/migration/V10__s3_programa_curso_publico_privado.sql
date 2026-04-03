ALTER TABLE programa_curso
    DROP CONSTRAINT uq_programa_curso_docente_periodo;

ALTER TABLE programa_curso
    ALTER COLUMN id_docente DROP NOT NULL;

CREATE UNIQUE INDEX uq_programa_curso_docente_periodo
    ON programa_curso (id_docente, codigo_curso, anio, semestre, seccion)
    WHERE id_docente IS NOT NULL;

CREATE UNIQUE INDEX uq_programa_curso_publico_periodo
    ON programa_curso (codigo_curso, anio, semestre, seccion)
    WHERE id_docente IS NULL;
