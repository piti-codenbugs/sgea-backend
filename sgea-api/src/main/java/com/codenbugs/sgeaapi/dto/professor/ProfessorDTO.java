package com.codenbugs.sgeaapi.dto.professor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfessorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
