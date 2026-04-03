package com.codenbugs.sgeaapi.repository.course;

import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeachingAssignmentCourseRepository extends JpaRepository<TeachingAssignmentCourse, Long> {
    List<TeachingAssignmentCourse> findByProfessorIdProfessor(Long professorId);

    boolean existsByProfessorIdProfessorAndCourseCodeAndPeriod(Long professorId, Short courseCode, String period);

    List<TeachingAssignmentCourse> findByProfessorIdProfessorAndPeriod(Long professorId, String period);

    @Modifying
    @Query("DELETE FROM TeachingAssignmentCourse t WHERE t.professor.idProfessor = :professorId AND t.period = :period")
    void deleteByProfessorIdAndPeriod(@Param("professorId") Long professorId, @Param("period") String period);
}
