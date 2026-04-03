package com.codenbugs.sgeaapi.dto.equivalencies;

import com.codenbugs.sgeaapi.enums.equivalencies.EquivalencyStatus;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EquivalencyRequestDTO {
    private Long id;
    private Short destinationCourseCode;
    private String destinationCourseName;
    private Long studentId;
    private String studentFullName;
    private Long professorId;
    private String professorFullName;
    private EquivalencyStatus status;
    private String comment;
    private String programUrl;
    private String courseCertificateUrl;
    private String signedProgramUrl;
    private String originCourseCode;
    private Integer year;
    private Integer semester;
    private String section;
    private LocalDateTime createdAt;
    private LocalDateTime resolutionDate;
}
