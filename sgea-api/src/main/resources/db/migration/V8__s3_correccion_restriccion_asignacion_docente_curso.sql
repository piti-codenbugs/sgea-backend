ALTER TABLE asignacion_docente_curso ADD COLUMN periodo VARCHAR(7);
ALTER TABLE asignacion_docente_curso ALTER COLUMN periodo SET NOT NULL;

ALTER TABLE asignacion_docente_curso DROP CONSTRAINT uq_docente_curso_fecha;
ALTER TABLE asignacion_docente_curso ADD CONSTRAINT uq_docente_curso_periodo UNIQUE (id_docente, codigo_curso, periodo);
