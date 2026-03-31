package com.codenbugs.sgeaapi.dto.professor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseAssignmentRequest {
    private Long professorId;
    private Short courseCode;
    private String period;
}
