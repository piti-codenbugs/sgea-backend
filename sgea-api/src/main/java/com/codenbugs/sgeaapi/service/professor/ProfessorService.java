package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.AccountStatusDTO;
import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import com.codenbugs.sgeaapi.entity.users.AccountStatusType;
import com.codenbugs.sgeaapi.entity.users.SessionHelper;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.exception.NotFoundException;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository repository;
    private final SessionHelper sessionHelper;

    public List<ProfessorDTO> getByStatus(AccountStatusType status ) {

        try {

            return repository
                    .findAllByAccountStatus_Status( status )
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

    public void updateAccount(Long id, AccountStatusDTO statusDTO){

        Professor p = repository.findById(id).orElseThrow(
                () -> new NotFoundException("El professor no existe")
        );

        AccountStatusType newStatus = statusDTO.getStatus();
        if (newStatus == AccountStatusType.PENDIENTE || newStatus == AccountStatusType.RECHAZADO ) {
            p.getUser().setActive(false);
        }

        AccountStatus accountStatus = p.getAccountStatus();
        accountStatus.setStatus(newStatus);
        accountStatus.setAdmin( sessionHelper.getCurrentUser() );
        accountStatus.setDate( LocalDateTime.now() );
        accountStatus.setComment( statusDTO.getComment() );

        repository.save(p);
    }
}
