package com.codenbugs.sgeaapi.dto.professor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class professorDTO {
    private String firstName;
    private String lastName;
    private String email;
}
