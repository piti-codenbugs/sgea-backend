package com.codenbugs.sgeaapi.repository.professor;

import com.codenbugs.sgeaapi.entity.course.TeachingAssignmentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseAssignmentRepository extends JpaRepository<TeachingAssignmentCourse, Long> {

}
