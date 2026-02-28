package com.codenbugs.sgeaapi.repository.student;

import com.codenbugs.sgeaapi.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
