package com.codenbugs.sgeaapi.dto.professor;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfessorAssignmentDTO {
    private Long id;
    private String professorName;
    private LocalDateTime assignmentDate;
}
