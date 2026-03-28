package com.codenbugs.sgeaapi.dto.professor;

import com.codenbugs.sgeaapi.entity.users.AccountStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProfessorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime registrationDate;
}
