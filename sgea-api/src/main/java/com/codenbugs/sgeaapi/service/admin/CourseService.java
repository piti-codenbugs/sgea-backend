package com.codenbugs.sgeaapi.service.admin;

import com.codenbugs.sgeaapi.dto.course.CourseDTO;
import com.codenbugs.sgeaapi.repository.course.CourseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.users.User;

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
    String professorName = null;
    Long professorId = null;

    if (course.getProfessor() != null) {
        professorId = course.getProfessor().getIdProfessor();

        User user = course.getProfessor().getUser();

        if (user != null) {
            String firstName = user.getFirstName() != null ? user.getFirstName() : "";
            String lastName = user.getLastName() != null ? user.getLastName() : "";

            professorName = (firstName + " " + lastName).trim();
        }
    }

    return CourseDTO.builder()
            .code(course.getCode())
            .name(course.getName())
            .careerId(course.getCareer().getId())
            .careerName(course.getCareer().getName())
            .professorId(professorId)
            .professorName(professorName)
            .build();
}
}
