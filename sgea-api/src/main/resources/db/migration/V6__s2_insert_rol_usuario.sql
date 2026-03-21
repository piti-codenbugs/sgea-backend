WITH roles_insertados AS (
    INSERT INTO rol (nombre, descripcion)
    VALUES 
        ('ROLE_ADMIN', 'Administrador del sistema'),
        ('ROLE_STUDENT', 'Estudiante'),
        ('ROLE_PROFESSOR', 'Catedrático')
    RETURNING id_rol, nombre
),

usuarios_insertados AS (
    INSERT INTO usuario (
        id_rol, email, password_hash, nombres, apellidos, fecha_registro, activo
    )
    SELECT
        r.id_rol,
        u.email,
        u.password,
        u.nombres,
        u.apellidos,
        u.fecha,
        u.activo
    FROM (
        VALUES
        ('ROLE_ADMIN', 'admin1@test.com', '$2a$10$9SsBzH2jnAP6q8C1vMpB3u0.cKoBjPMSD5QkvmN1Pl/90hZoNj4P.', 'admin1', 'admin1', NOW(), true),
        ('ROLE_STUDENT', 'estudiante1@test.com', '$2a$10$9SsBzH2jnAP6q8C1vMpB3u0.cKoBjPMSD5QkvmN1Pl/90hZoNj4P.', 'Estudiante', 'Prueba', NOW(), true),
        ('ROLE_PROFESSOR', 'docente1@test.com', '$2a$10$9SsBzH2jnAP6q8C1vMpB3u0.cKoBjPMSD5QkvmN1Pl/90hZoNj4P.', 'Docente', 'Prueba', NOW(), true)
    ) AS u(rol_nombre, email, password, nombres, apellidos, fecha, activo)
    JOIN roles_insertados r ON r.nombre = u.rol_nombre
    RETURNING id_usuario, id_rol
),

usuarios_con_rol AS (
    SELECT ui.id_usuario, r.nombre AS rol_nombre
    FROM usuarios_insertados ui
    JOIN roles_insertados r ON r.id_rol = ui.id_rol
),

insert_docente AS (
    INSERT INTO docente (id_usuario)
    SELECT id_usuario
    FROM usuarios_con_rol
    WHERE rol_nombre = 'ROLE_PROFESSOR'
),

insert_estudiante AS (
    INSERT INTO estudiante (id_usuario, carnet)
    SELECT id_usuario, 200000002
    FROM usuarios_con_rol
    WHERE rol_nombre = 'ROLE_STUDENT'
)

SELECT 1;