package com.codenbugs.sgeaapi.entity.equivalencies;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "programa_curso")
public class ProgramCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = true)
    private Professor professor;

    @Column(name = "codigo_curso_origen", nullable = false, length = 30)
    private String originCourseCode;

    @Column(name = "anio", nullable = false)
    private Integer year;

    @Column(name = "semestre", nullable = false)
    private Integer semester;

    @Column(name = "seccion", nullable = false, length = 10)
    private String section;

    @Column(name = "url_programa", nullable = false)
    private String programUrl;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
