package com.codenbugs.sgeaapi.repository.equivalencies;

import com.codenbugs.sgeaapi.entity.equivalencies.ProgramCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramCourseRepository extends JpaRepository<ProgramCourse, Long> {
    List<ProgramCourse> findByCourseCodeOrderByCreatedAtDesc(Short courseCode);
}
