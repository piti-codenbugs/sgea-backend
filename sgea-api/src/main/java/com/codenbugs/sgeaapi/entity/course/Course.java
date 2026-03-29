package com.codenbugs.sgeaapi.entity.course;

import com.codenbugs.sgeaapi.entity.career.Career;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 *
 * @author ronyrojas
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "curso")
public class Course {

    @Id
    @Column(name = "codigo")
    private Short code;

    @Column(name = "nombre", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Career career;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<TeachingAssignmentCourse> assignments;
}