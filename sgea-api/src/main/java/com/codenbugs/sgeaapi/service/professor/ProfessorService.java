package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.enums.Status;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository repository;

    public List<ProfessorDTO> getByStatus(Status status ) {

        try {
            boolean isActive = status == Status.ACTIVE;

            return repository
                    .findAllByUserActive(isActive)
                    .stream()
                    .map(professor -> ProfessorDTO.builder()
                            .id(professor.getIdProfessor())
                            .firstName(professor.getUser().getFirstName())
                            .lastName(professor.getUser().getLastName())
                            .email(professor.getUser().getEmail())
                            .build())
                    .toList();

        }catch (IllegalArgumentException e){
            throw new InvalidArgumentException("El estado ingresado es invalido, estado: <" + status + ">");
        }
    }
}
