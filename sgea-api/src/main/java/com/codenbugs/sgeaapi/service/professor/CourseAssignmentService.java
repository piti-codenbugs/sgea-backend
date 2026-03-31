package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.CourseAssignmentRequest;
import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.repository.course.CourseRepository;
import com.codenbugs.sgeaapi.repository.course.TeachingAssignmentCourseRepository;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {
    private final TeachingAssignmentCourseRepository assignmentCourseRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public ProfessorAssignmentDTO createAssignment(CourseAssignmentRequest request) {
        if (assignmentCourseRepository.existsByIdAndCourseAndPeriod(request.getProfessorId(), request.getCourseCode(), request.getPeriod())) {
            throw new RuntimeException("El docente ya está asignado a este curso en el periodo: " + request.getPeriod());
        }

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Course course = courseRepository.findById(request.getCourseCode())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        TeachingAssignmentCourse assignment = TeachingAssignmentCourse.builder()
                .professor(professor)
                .course(course)
                .period(request.getPeriod())
                .build();

        return mapToDTO(assignmentCourseRepository.save(assignment));
    }

    public List<ProfessorAssignmentDTO> getAllAssignments() {
    }

    public List<ProfessorAssignmentDTO> getAssignmentsByCurrentProfessor() {
    }

    public ProfessorAssignmentDTO updateAssignment(Long professorId, CourseAssignmentRequest request) {
    }

    public void deleteAssignment(Long id) {
    }

    private ProfessorAssignmentDTO mapToDTO(TeachingAssignmentCourse entity) {
        return ProfessorAssignmentDTO.builder()
                .id(entity.getId())
                .professorName(entity.getProfessor().getUser().getFirstName() + " " + entity.getProfessor().getUser().getLastName())
                .courseName(entity.getCourse().getName())
                .courseCode(entity.getCourse().getCode())
                .period(entity.getPeriod())
                .assignmentDate(entity.getFechaAsignacion())
                .build();
    }
}
