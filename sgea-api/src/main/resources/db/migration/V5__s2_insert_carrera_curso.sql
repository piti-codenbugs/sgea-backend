WITH carrera_insertada AS (
    INSERT INTO carrera (nombre, descripcion)
    VALUES (
        'Ingeniería en Sistemas',
        'Carrera que diseña, desarrolla, implementa y gestiona soluciones tecnológicas.'
    )
    RETURNING id_carrera
)
INSERT INTO curso (codigo, nombre, id_carrera, id_docente)
SELECT 
    c.codigo,
    c.nombre,
    ci.id_carrera,
    NULL
FROM carrera_insertada ci,
(
    VALUES
    --1er semestre
    (3003, 'Área Social Humanistica 1'),
    (3000, 'Área Matemática Básica 1'),
    (3005, 'Técnicas de Estudio e Investigación'),
    --2do semestre
    (3011, 'Área Social Humanistica 2'),
    (3006, 'Área Matemática Básica 2'),
    (3231, 'Matemática para computación 1'),
    (3007, 'Física Básica'),
    --3er semestre
    (3232, 'Lógica de Sistemas'),
    (3233, 'Matemática para computación 2'),
    (3234, 'Introducción a la Programación y Computación 1'),
    (3013, 'Área Matemática Intermedia 1'),
    (3014, 'Física 1'),
    --4to semestre
    (3235, 'Lenguajes Formales y de Programación'),
    (3236, 'Introducción a la Programación y Computación 2'),
    (3021, 'Área Matemática Intermedia 2'),
    (3022, 'Área Matemática Intermedia 3'),
    (3023, 'Física 2'),
    --5to semestre
    (3090, 'Estadística 1'),
    (3239, 'Organización de Lenguajes y Compiladores 1'),
    (3240, 'Organización Computacional'),
    (3241, 'Estructura de Datos'),
    (3027, 'Matemática Aplicada 3'),
    (3028, 'Matemática Aplicada 1'),
    --6to semestre
    (3242, 'Teoría de Sistemas 1'),
    (3243, 'Investigación de Operaciones 1'),
    (3244, 'Economía'),
    (3245, 'Organización de Lenguajes y Compiladores 2'),
    (3246, 'Arquitectura de Computadoras y Ensambladores 1'),
    (3247, 'Manejo e Implementación de Archivos'),
    --7mo semestre
    (3248, 'Teoría de Sistemas 2'),
    (3249, 'Investigación de Operaciones 2'),
    (3250, 'Sistemas Operativos 1'),
    (3251, 'Arquitectura de Computadoras y Ensambladores 2'),
    (3252, 'Redes de Computadoras 1'),
    (3253, 'Sistemas de Bases de Datos 1'),
    (3254, 'Prácticas Intermedias'),
    --8vo semestre
    (3255, 'Sistemas Operativos 2'),
    (3256, 'Redes de Computadoras 2'),
    (3257, 'Sistemas de Bases de Datos 2'),
    (3258, 'Análisis y Diseño de Sistemas 1'),
    (3259, 'Seminario de Sistemas 1'),
    --9no semestre
    (3260, 'Modelación y Simulación 1'),
    (3261, 'Sistemas Organizacionales y Gerenciales 1'),
    (3262, 'Inteligencia Artificial 1'),
    (3264, 'Análisis y Diseño de Sistemas 2'),
    (3265, 'Seminario de Sistemas 2'),
    (3268, 'Prácticas Finales Ingeniería Ciencias y Sistemas'),
    --10mo semestre
    (3269, 'Modelación y Simulación 2'),
    (3270, 'Sistemas Organizacionales y Gerenciales 2'),
    (3274, 'Software Avanzado'),
    (3278, 'Seminario de Investigación')
) AS c(codigo, nombre);