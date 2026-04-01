package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.controller.course.CourseAssignmentRequest;
import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.SessionHelper;
import com.codenbugs.sgeaapi.exception.AssignmentExistException;
import com.codenbugs.sgeaapi.exception.CourseDoesNotExistException;
import com.codenbugs.sgeaapi.exception.ProfessorDoesNotExistException;
import com.codenbugs.sgeaapi.exception.RegisterDoesNotExistException;
import com.codenbugs.sgeaapi.repository.course.CourseRepository;
import com.codenbugs.sgeaapi.repository.course.TeachingAssignmentCourseRepository;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {
    private final TeachingAssignmentCourseRepository assignmentCourseRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final SessionHelper sessionHelper;

    @Transactional
    public ProfessorAssignmentDTO createAssignment(CourseAssignmentRequest request) {
        if (assignmentCourseRepository.existsByProfessorIdProfessorAndCourseCodeAndPeriod(request.getProfessorId(), request.getCourseCode(), request.getPeriod())) {
            throw new AssignmentExistException("El docente ya está asignado a este curso en el periodo: " + request.getPeriod());
        }

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ProfessorDoesNotExistException("Docente no encontrado"));

        Course course = courseRepository.findById(request.getCourseCode())
                .orElseThrow(() -> new CourseDoesNotExistException("Curso no encontrado"));

        TeachingAssignmentCourse assignment = TeachingAssignmentCourse.builder()
                .professor(professor)
                .course(course)
                .period(request.getPeriod())
                .build();

        return mapToDTO(assignmentCourseRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public List<ProfessorAssignmentDTO> getAllAssignments() {
        return assignmentCourseRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfessorAssignmentDTO> getAssignmentsByCurrentProfessor() {
        Long userId = sessionHelper.getCurrentUser().getIdUser();

        if (!professorRepository.existsById(userId)) {
            throw new ProfessorDoesNotExistException("El usuario logueado no tiene un perfil de docente.");
        }

        return assignmentCourseRepository.findByProfessorIdProfessor(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProfessorAssignmentDTO updateAssignment(Long professorId, CourseAssignmentRequest request) {
        TeachingAssignmentCourse assignment = assignmentCourseRepository.findById(professorId)
                .orElseThrow(() -> new RegisterDoesNotExistException("Asignación no encontrada"));

        Course course = courseRepository.findById(request.getCourseCode())
                .orElseThrow(() -> new CourseDoesNotExistException("Curso no encontrado"));

        assignment.setCourse(course);
        assignment.setPeriod(request.getPeriod());

        return mapToDTO(assignmentCourseRepository.save(assignment));
    }

    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentCourseRepository.existsById(id)) {
            throw new RegisterDoesNotExistException("No se puede borrar: El registro no existe");
        }
        assignmentCourseRepository.deleteById(id);
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
