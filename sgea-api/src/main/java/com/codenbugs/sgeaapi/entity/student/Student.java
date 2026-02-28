package com.codenbugs.sgeaapi.entity.student;

import com.codenbugs.sgeaapi.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "students", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_student"})})
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student",  nullable = false, unique = true)
    private Long idStudent;

    @Column(name = "carnet",   nullable = false, unique = true)
    private String carnet;

    @OneToOne
    @JoinColumn(name = "id_user",  nullable = false, unique = true)
    private User user;
}
