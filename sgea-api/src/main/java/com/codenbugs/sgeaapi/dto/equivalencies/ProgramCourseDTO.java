package com.codenbugs.sgeaapi.dto.equivalencies;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProgramCourseDTO {
    private Long id;
    private String courseCode;
    private String courseName;
    private Long professorId;
    private String professorName;
    private Integer year;
    private Integer semester;
    private String section;
    private String programUrl;
    private LocalDateTime createdAt;
}
