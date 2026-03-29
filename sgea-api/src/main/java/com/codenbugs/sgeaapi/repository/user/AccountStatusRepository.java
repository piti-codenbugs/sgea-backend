package com.codenbugs.sgeaapi.repository.user;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatus, Long> {
    Optional<AccountStatus> findByProfessor(Professor professor);
}
