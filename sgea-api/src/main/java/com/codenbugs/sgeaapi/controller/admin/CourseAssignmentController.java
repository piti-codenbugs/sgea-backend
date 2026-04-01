package com.codenbugs.sgeaapi.controller.admin;

import com.codenbugs.sgeaapi.controller.course.CourseAssignmentRequest;
import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import com.codenbugs.sgeaapi.service.professor.CourseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/course-assignment")
@RequiredArgsConstructor
public class CourseAssignmentController {
    private final CourseAssignmentService courseAssignmentService;

    /**
     * Crea una nueva asignación de curso a un docente.
     *
     * @param request contiene el JSON con los datos requeridos para la asignación.
     * @return un estado HTTP con estado 201.
     */
    @PostMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorAssignmentDTO> createAssignment(@RequestBody CourseAssignmentRequest request) {
        return new ResponseEntity<>(courseAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    /**
     * Permite obtener todas las asignaciones de cursos que tiene cada docente.
     *
     * @return un JSON con todos los datos en la base de datos.
     */
    @GetMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfessorAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(courseAssignmentService.getAllAssignments());
    }

    /**
     * Obtiene todos los cursos que el docente logueado tenga asignados.
     *
     * @return el JSON cont todos los cursos asignados.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    @GetMapping("/my-courses")
    public ResponseEntity<List<ProfessorAssignmentDTO>> getMyCourses() {
        //Validar usuario logueado
        return ResponseEntity.ok(courseAssignmentService.getAssignmentsByCurrentProfessor());
    }

    /**
     * Permite actualizar la asignación de un docente.
     *
     * @param id      es el ID del docente que se actualizará.
     * @param request es el JSON con los datos del curso.
     * @return un estado HTTP del resultado de la operación.
     */
    @PutMapping("/assignments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfessorAssignmentDTO> updateAssignment(@PathVariable("id") Long id, @RequestBody CourseAssignmentRequest request) {
        return ResponseEntity.ok(courseAssignmentService.updateAssignment(id, request));
    }

    /**
     * Permite eliminar una asignación específica por su ID.
     *
     * @param id es el ID del docente que se eliminará el registro.
     * @return un estado HTTP 204 si la eliminación fue exitosa.
     */
    @DeleteMapping("/assignments/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAssignment(@PathVariable("id") Long id) {
        courseAssignmentService.deleteAssignment(id);
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "Asignación eliminada exitosamente");
        response.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}