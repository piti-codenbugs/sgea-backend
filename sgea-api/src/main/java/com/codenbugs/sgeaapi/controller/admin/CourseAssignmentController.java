package com.codenbugs.sgeaapi.controller.admin;

import com.codenbugs.sgeaapi.dto.professor.CourseAssignmentRequest;
import com.codenbugs.sgeaapi.dto.professor.ProfessorAssignmentDTO;
import com.codenbugs.sgeaapi.service.professor.CourseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/course-assignment")
@RequiredArgsConstructor
public class CourseAssignmentController {
    private final CourseAssignmentService courseAssignmentService;

    /**
     * Crea una nueva asignación de curso a un docente.
     * @param request contiene el JSON con los datos requeridos para la asignación.
     * @return un estado HTTP con estado 201.
     */
    @PostMapping("/assignments")
    public ResponseEntity<ProfessorAssignmentDTO> createAssignment(@RequestBody CourseAssignmentRequest request) {
        return new ResponseEntity<>(courseAssignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    /**
     * Permite obtener todas las asignaciones de cursos que tiene cada docente.
     * @return un JSON con todos los datos en la base de datos.
     */
    @GetMapping("/assignments")
    public ResponseEntity<List<ProfessorAssignmentDTO>> getAllAssignments() {
        return ResponseEntity.ok(courseAssignmentService.getAllAssignments());
    }

    /**
     * Obtiene todos los cursos que el docente logueado tenga asignados.
     * @return el JSON cont todos los cursos asignados.
     */
    @GetMapping("/my-courses")
    public ResponseEntity<List<ProfessorAssignmentDTO>> getMyCourses() {
        //Validar usuario logueado
        return ResponseEntity.ok(courseAssignmentService.getAssignmentsByCurrentProfessor());
    }

    /**
     * Permite actualizar la asignación de un docente.
     * @param professorId es el ID del docente que se actualizará.
     * @param request es el JSON con los datos del curso.
     * @return un estado HTTP del resultado de la operación.
     */
    @PutMapping("/assignments/{professorId}")
    public ResponseEntity<ProfessorAssignmentDTO> updateAssignment(@PathVariable("professorId") Long professorId, @RequestBody CourseAssignmentRequest request) {
        return ResponseEntity.ok(courseAssignmentService.updateAssignment(professorId, request));
    }

    /**
     * Permite eliminar una asignación específica por su ID.
     * @param id es el ID del docente que se eliminará el registro.
     * @return un estod HTTP 204 si la eliminación fue exitosa.
     */
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable("id") Long id) {
        courseAssignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}