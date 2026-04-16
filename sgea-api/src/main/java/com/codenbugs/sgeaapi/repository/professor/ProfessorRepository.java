package com.codenbugs.sgeaapi.repository.professor;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.enums.AccountStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    List<Professor> findAllByAccountStatus_Status(AccountStatusType accountStatusStatus);
}