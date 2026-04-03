package com.codenbugs.sgeaapi.controller.course;

import com.codenbugs.sgeaapi.dto.course.CourseDTO;
import com.codenbugs.sgeaapi.service.admin.CourseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ronyrojas
 */
@RestController
@RequestMapping("/api/v1/admin/cursos")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    /**
     * Endpoint para obtener todos los cursos disponibles.
     * Puede ser consultado por administradores y estudiantes.
     * @return un objeto de tipo ResponseEntity, que es la lista de cursos
     */
    @PreAuthorize("hasAnyRole('ADMIN','ROLE_STUDENT')")
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAll());
    }
}