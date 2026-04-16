CREATE TABLE carrera (
    id_carrera SMALLSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE curso (
    codigo SMALLINT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    id_carrera SMALLINT NOT NULL,
    id_docente BIGINT NOT NULL,
    CONSTRAINT fk_curso_carrera FOREIGN KEY (id_carrera) REFERENCES carrera(id_carrera),
    CONSTRAINT fk_curso_docente FOREIGN KEY (id_docente) REFERENCES docente(id_usuario)
);