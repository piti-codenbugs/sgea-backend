package com.codenbugs.sgeaapi.entity.docente;

import java.util.List;

import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import com.codenbugs.sgeaapi.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "docente")
public class Professor {
    @Id
    @Column(name = "id_usuario")
    private Long idProfessor;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;

    @OneToOne( mappedBy = "professor", fetch = FetchType.LAZY)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private List<TeachingAssignmentCourse> assignments;
}
