CREATE TABLE rol (
    id_rol BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    id_rol BIGINT NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT NOW(),
    activo BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE docente (
    id_usuario BIGINT PRIMARY KEY,
    CONSTRAINT fk_docente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE estudiante (
    id_usuario BIGINT PRIMARY KEY,
    carnet VARCHAR(15) UNIQUE NOT NULL,
    CONSTRAINT fk_estudiante_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);
