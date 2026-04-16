package com.codenbugs.sgeaapi.entity.equivalencies;

import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.student.Student;
import com.codenbugs.sgeaapi.enums.equivalencies.EquivalencyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "solicitud_equivalencia")
public class EquivalencyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_curso_destino", nullable = false)
    private Course destinationCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Professor professor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EquivalencyStatus status;

    @Column(name = "comentario")
    private String comment;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "fecha_resolucion")
    private LocalDateTime resolutionDate;

    @Column(name = "url_programa", nullable = false)
    private String programUrl;

    @Column(name = "url_constancia_cursos", nullable = false)
    private String courseCertificateUrl;

    @Column(name = "url_programa_firmado")
    private String signedProgramUrl;

    @Column(name = "codigo_curso_origen", length = 30)
    private String originCourseCode;

    @Column(name = "anio")
    private Integer year;

    @Column(name = "semestre")
    private Integer semester;

    @Column(name = "seccion", length = 10)
    private String section;
}
