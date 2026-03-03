package com.codenbugs.sgeaapi.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "rol", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_role"})})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol",  unique = true, nullable = false)
    private Long idRole;
    @Column(name = "nombre", nullable = false)
    private String name;
    @Column(name = "descripcion")
    private String description;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<User> users;
}