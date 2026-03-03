package com.codenbugs.sgeaapi.repository.professor;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}