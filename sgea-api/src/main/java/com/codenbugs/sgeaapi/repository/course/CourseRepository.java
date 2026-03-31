package com.codenbugs.sgeaapi.repository.course;

import com.codenbugs.sgeaapi.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ronyrojas
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Short> {

    Course findByCode(Short code);
}