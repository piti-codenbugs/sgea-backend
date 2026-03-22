package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.enums.Status;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.exception.NotFoundException;
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
                            .registrationDate( professor.getUser().getRegistrationDate() )
                            .build())
                    .toList();

        }catch (IllegalArgumentException e){
            throw new InvalidArgumentException("El estado ingresado es invalido, estado: <" + status + ">");
        }
    }

    public ProfessorDTO getById(Long id) {
        Professor p = repository.findById(id).orElseThrow(
                () ->  new InvalidArgumentException("El professor no existe")
        );

        return ProfessorDTO.builder()
                .id( p.getIdProfessor() )
                .firstName( p.getUser().getFirstName() )
                .lastName( p.getUser().getLastName() )
                .email( p.getUser().getEmail() )
                .registrationDate( p.getUser().getRegistrationDate() )
                .build();
    }

    public void approve( Long id){

        Professor p = repository.findById(id).orElseThrow(
                () -> new NotFoundException("El professor no existe")
        );

        p.getUser().setActive(true);

        repository.save(p);
    }
}
