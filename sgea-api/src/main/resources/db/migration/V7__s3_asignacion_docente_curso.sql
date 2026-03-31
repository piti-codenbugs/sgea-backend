CREATE TABLE asignacion_docente_curso (
	id BIGSERIAL PRIMARY KEY,
	id_docente BIGINT NOT NULL,
	codigo_curso SMALLINT NOT NULL,
	fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_asignacion_docente FOREIGN KEY (id_docente) REFERENCES docente(id_usuario),
	CONSTRAINT fk_asignacion_curso FOREIGN KEY (codigo_curso) REFERENCES curso(codigo),
	CONSTRAINT uq_docente_curso_fecha UNIQUE (id_docente, codigo_curso, fecha_asignacion)
);

ALTER TABLE curso DROP CONSTRAINT fk_curso_docente;
ALTER TABLE curso DROP COLUMN id_docente;
