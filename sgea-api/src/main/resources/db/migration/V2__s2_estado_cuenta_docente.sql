CREATE TABLE estado_cuenta(
	id_estado BIGSERIAL PRIMARY KEY,
	id_admin_fk BIGINT,
	id_docente_fk BIGINT NOT NULL,
	estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' 
		CHECK(estado IN('PENDIENTE','APROBADO','RECHAZADO')),
	comentario TEXT,
	fecha TIMESTAMP NOT NULL DEFAULT NOW(),
	CONSTRAINT fk_estado_docente FOREIGN KEY (id_docente_fk) REFERENCES docente(id_usuario),
	CONSTRAINT fk_estado_admin FOREIGN KEY (id_admin_fk) REFERENCES usuario(id_usuario)
);

CREATE OR REPLACE FUNCTION fn_crear_estado_inicial()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO estado_cuenta (id_docente_fk, estado, fecha)
    VALUES (NEW.id_usuario, 'PENDIENTE', NOW());
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_estado_inicial
AFTER INSERT ON docente
FOR EACH ROW
EXECUTE FUNCTION fn_crear_estado_inicial();

CREATE OR REPLACE FUNCTION fn_activar_usuario_docente()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.estado = 'APROBADO' THEN
        UPDATE usuario SET activo = true
        WHERE id_usuario = NEW.id_docente_fk;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_activar_usuario
AFTER INSERT OR UPDATE ON estado_cuenta
FOR EACH ROW
EXECUTE FUNCTION fn_activar_usuario_docente();
