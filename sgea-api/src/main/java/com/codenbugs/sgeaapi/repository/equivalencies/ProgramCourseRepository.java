package com.codenbugs.sgeaapi.repository.equivalencies;

import com.codenbugs.sgeaapi.entity.equivalencies.ProgramCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {
    List<ProgramCourse> findByProfessorIsNullAndOriginCourseCodeContainingIgnoreCaseOrderByCreatedAtDesc(
            String courseCode);

    List<ProgramCourse> findByProfessorIdProfessorAndOriginCourseCodeContainingIgnoreCaseOrderByCreatedAtDesc(
            Long professorId,
            String courseCode);

    boolean existsByProfessorIdProfessorAndOriginCourseCodeAndYearAndSemesterAndSection(
            Long professorId,
            String originCourseCode,
            Integer year,
            Integer semester,
            String section);

    boolean existsByProfessorIsNullAndOriginCourseCodeAndYearAndSemesterAndSection(
            String originCourseCode,
            Integer year,
            Integer semester,
            String section);
}
