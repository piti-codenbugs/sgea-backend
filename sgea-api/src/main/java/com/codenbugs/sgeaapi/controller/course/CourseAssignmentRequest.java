package com.codenbugs.sgeaapi.controller.course;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseAssignmentRequest {
    private Long professorId;
    private List<Short> courseCodes;
    private String period;
}
