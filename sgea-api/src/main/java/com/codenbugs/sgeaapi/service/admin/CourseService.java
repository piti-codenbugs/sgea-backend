package com.codenbugs.sgeaapi.service.admin;

import com.codenbugs.sgeaapi.dto.course.CourseDTO;
import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.repository.course.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ronyrojas
 */
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;


    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream().map(this::toDTO).toList();
    }

    private CourseDTO toDTO(Course course) {
        List<ProfessorAssignmentDTO> professors = course.getAssignments().stream()
                .map((TeachingAssignmentCourse assignment) -> {
                    Professor professor = assignment.getProfessor();
                    User user = professor.getUser();

                    String fullName = (user != null) ? (user.getFirstName() + " " + user.getLastName()).trim() : "Sin nombre";

                    return ProfessorAssignmentDTO.builder()
                            .id(professor.getIdProfessor())
                            .professorName(fullName)
                            .assignmentDate(LocalDateTime.parse(assignment.getFechaAsignacion() != null ? assignment.getFechaAsignacion().toString() : "Fecha no disponible"))
                            .build();
                }).toList();

        return CourseDTO.builder()
                .code(course.getCode())
                .name(course.getName())
                .careerId(course.getCareer().getId())
                .careerName(course.getCareer().getName())
                .assignedProfessors(professors)
                .build();
    }
}