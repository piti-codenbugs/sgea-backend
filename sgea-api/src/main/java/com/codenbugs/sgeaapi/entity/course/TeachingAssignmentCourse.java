package com.codenbugs.sgeaapi.entity.course;

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
@Table(name = "asignacion_docente_curso")
public class TeachingAssignmentCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Professor professor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_curso", nullable = false)
    private Course course;

    @Column(name = "fecha_asignacion", updatable = false, insertable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "periodo", nullable = false, length = 7)
    private String period;
}
