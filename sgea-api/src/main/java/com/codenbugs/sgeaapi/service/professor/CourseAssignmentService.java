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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseAssignmentService {
    private final TeachingAssignmentCourseRepository assignmentCourseRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final SessionHelper sessionHelper;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<ProfessorAssignmentDTO> createAssignment(CourseAssignmentRequest request) {
        List<ProfessorAssignmentDTO> createdDTOs = new ArrayList<>();

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ProfessorDoesNotExistException("Docente no encontrado"));

        for (Short courseCode : request.getCourseCodes()) {

            if (assignmentCourseRepository.existsByProfessorIdProfessorAndCourseCodeAndPeriod(
                    request.getProfessorId(), courseCode, request.getPeriod())) {
                throw new AssignmentExistException("El docente ya tiene asignado el curso " + courseCode);
            }

            Course course = courseRepository.findById(courseCode)
                    .orElseThrow(() -> new CourseDoesNotExistException("Curso " + courseCode + " no encontrado"));

            TeachingAssignmentCourse assignment = TeachingAssignmentCourse.builder()
                    .professor(professor)
                    .course(course)
                    .period(request.getPeriod())
                    .build();

            TeachingAssignmentCourse savedEntity = assignmentCourseRepository.saveAndFlush(assignment);

            entityManager.detach(savedEntity);

            TeachingAssignmentCourse finalEntity = assignmentCourseRepository.findById(savedEntity.getId())
                    .orElseThrow(() -> new RegisterDoesNotExistException("Error al recuperar la asignación guardada"));

            createdDTOs.add(mapToDTO(finalEntity));
        }

        return createdDTOs;
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
    public List<ProfessorAssignmentDTO> updateAssignment(CourseAssignmentRequest request) {
        List<ProfessorAssignmentDTO> updatedAssignments = new ArrayList<>();

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ProfessorDoesNotExistException("Docente no encontrado"));

        assignmentCourseRepository.deleteByProfessorIdAndPeriod(request.getProfessorId(), request.getPeriod());

        entityManager.flush();
        entityManager.clear();

        for (Short courseCode : request.getCourseCodes()) {
            Course course = courseRepository.findById(courseCode)
                    .orElseThrow(() -> new CourseDoesNotExistException("Curso " + courseCode + " no encontrado"));

            TeachingAssignmentCourse newAssignment = TeachingAssignmentCourse.builder()
                    .professor(professor)
                    .course(course)
                    .period(request.getPeriod())
                    .build();

            TeachingAssignmentCourse saved = assignmentCourseRepository.saveAndFlush(newAssignment);

            entityManager.detach(saved);

            TeachingAssignmentCourse finalEntity = assignmentCourseRepository.findById(saved.getId())
                    .orElseThrow(() -> new RegisterDoesNotExistException("Error al recuperar la nueva asignación"));

            updatedAssignments.add(mapToDTO(finalEntity));
        }

        return updatedAssignments;
    }

    @Transactional
    public Long deleteAssignment(Long id) {
        if (!assignmentCourseRepository.existsById(id)) {
            throw new RegisterDoesNotExistException("No se puede borrar: El registro no existe");
        }
        assignmentCourseRepository.deleteById(id);
        return id;
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
