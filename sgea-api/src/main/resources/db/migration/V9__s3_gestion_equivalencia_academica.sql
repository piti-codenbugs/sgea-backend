CREATE TABLE programa_curso (
	id BIGSERIAL PRIMARY KEY,
	id_docente BIGINT NOT NULL,
	codigo_curso SMALLINT NOT NULL,
	anio INT NOT NULL,
	semestre INT NOT NULL,
	seccion VARCHAR(10) NOT NULL,
	url_programa TEXT NOT NULL,
	fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_programa_curso_docente FOREIGN KEY (id_docente) REFERENCES docente(id_usuario),
	CONSTRAINT fk_programa_curso_curso FOREIGN KEY (codigo_curso) REFERENCES curso(codigo),
	CONSTRAINT ck_programa_curso_semestre CHECK (semestre BETWEEN 1 AND 2),
	CONSTRAINT ck_programa_curso_anio CHECK (anio >= 2000),
	CONSTRAINT uq_programa_curso_docente_periodo UNIQUE (id_docente, codigo_curso, anio, semestre, seccion)
);

CREATE TABLE solicitud_equivalencia (
	id BIGSERIAL PRIMARY KEY,
	codigo_curso_destino SMALLINT NOT NULL,
	id_estudiante BIGINT NOT NULL,
	id_docente BIGINT NOT NULL,
	estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
		CHECK (estado IN ('PENDIENTE', 'ACEPTADO', 'RECHAZADO')),
	comentario TEXT,
	fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
	fecha_resolucion TIMESTAMP,
	url_programa TEXT NOT NULL,
	url_constancia_cursos TEXT NOT NULL,
	url_programa_firmado TEXT,
	codigo_curso_origen VARCHAR(30),
	anio INT,
	semestre INT,
	seccion VARCHAR(10),
	CONSTRAINT fk_solicitud_equivalencia_curso_destino FOREIGN KEY (codigo_curso_destino) REFERENCES curso(codigo),
	CONSTRAINT fk_solicitud_equivalencia_estudiante FOREIGN KEY (id_estudiante) REFERENCES estudiante(id_usuario),
	CONSTRAINT fk_solicitud_equivalencia_docente FOREIGN KEY (id_docente) REFERENCES docente(id_usuario),
	CONSTRAINT ck_solicitud_equivalencia_semestre CHECK (semestre IS NULL OR semestre BETWEEN 1 AND 2),
	CONSTRAINT ck_solicitud_equivalencia_anio CHECK (anio IS NULL OR anio >= 2000),
	CONSTRAINT ck_solicitud_equivalencia_resolucion CHECK (
		(estado = 'PENDIENTE' AND fecha_resolucion IS NULL)
		OR (estado IN ('ACEPTADO', 'RECHAZADO') AND fecha_resolucion IS NOT NULL)
	)
);

CREATE INDEX idx_programa_curso_docente ON programa_curso(id_docente);
CREATE INDEX idx_programa_curso_codigo_curso ON programa_curso(codigo_curso);

CREATE INDEX idx_solicitud_equivalencia_estado ON solicitud_equivalencia(estado);
CREATE INDEX idx_solicitud_equivalencia_estudiante ON solicitud_equivalencia(id_estudiante);
CREATE INDEX idx_solicitud_equivalencia_docente ON solicitud_equivalencia(id_docente);
CREATE INDEX idx_solicitud_equivalencia_curso_destino ON solicitud_equivalencia(codigo_curso_destino);
