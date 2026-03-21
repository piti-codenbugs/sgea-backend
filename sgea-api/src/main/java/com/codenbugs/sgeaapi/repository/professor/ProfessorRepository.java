package com.codenbugs.sgeaapi.repository.professor;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findAllByUserActive(boolean isActive);
}