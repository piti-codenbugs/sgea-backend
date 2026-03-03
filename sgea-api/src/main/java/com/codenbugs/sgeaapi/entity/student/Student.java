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
@Table(name = "estudiante")
public class Student {
    @Id
    @Column(name = "id_usuario")
    private Long idStudent;

    @Column(name = "carnet",   nullable = false, unique = true)
    private String carnet;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private User user;
}
