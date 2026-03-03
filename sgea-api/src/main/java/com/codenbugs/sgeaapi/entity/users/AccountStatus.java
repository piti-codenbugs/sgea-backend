package com.codenbugs.sgeaapi.entity.users;

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
@Table(name = "estado_cuenta")
public class AccountStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_docente_fk", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "id_admin_fk")
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private AccountStatusType status;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;
}
