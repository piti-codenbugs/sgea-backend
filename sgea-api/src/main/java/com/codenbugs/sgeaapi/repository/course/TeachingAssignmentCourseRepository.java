package com.codenbugs.sgeaapi.repository.course;

import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingAssignmentCourseRepository extends JpaRepository<TeachingAssignmentCourse, Long> {
    List<TeachingAssignmentCourse> findByProfessorIdProfessor(Long professorId);

    boolean existsByProfessorIdProfessorAndCourseCodeAndPeriod(Long professorId, Short courseCode, String period);

    List<TeachingAssignmentCourse> findByProfessorIdProfessorAndPeriod(Long professorId, String period);
}
