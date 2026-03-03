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
@Table(name = "docente")
public class Professor {
    @Id
    @Column(name = "id_usuario")
    private Long idProfessor;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private User user;
}
