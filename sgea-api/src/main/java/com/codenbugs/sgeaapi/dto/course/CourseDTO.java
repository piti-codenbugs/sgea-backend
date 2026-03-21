package com.codenbugs.sgeaapi.dto.course;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author ronyrojas
 */
@Data
@Builder
public class CourseDTO {

    private Short code;
    private String name;
    private Short careerId;
    private String careerName;
    private Long professorId;
    private String professorName;
}