package com.codenbugs.sgeaapi.entity.docente;

import com.codenbugs.sgeaapi.entity.users.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "professor", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_professor"})})
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfessor;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;
}
