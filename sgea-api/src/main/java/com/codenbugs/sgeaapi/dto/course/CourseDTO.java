package com.codenbugs.sgeaapi.dto.course;

import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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
    private List<ProfessorAssignmentDTO> assignedProfessors;
}