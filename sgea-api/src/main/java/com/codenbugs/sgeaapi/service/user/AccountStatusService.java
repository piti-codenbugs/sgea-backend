package com.codenbugs.sgeaapi.service.user;

import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import com.codenbugs.sgeaapi.repository.user.AccountStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountStatusService {
    @Autowired
    private AccountStatusRepository accountStatusRepository;

    public AccountStatus getByProfessor(Professor professor) {
        return accountStatusRepository.findByProfessor(professor).orElseThrow(() -> new RuntimeException("Estado de cuenta no encontrado para docente con ID: " + professor.getIdProfessor()));
    }
}
