package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.enums.Status;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final UserRepository userRepository;

    public List<ProfessorDTO> getByStatus(Status status ) {

        try {
            boolean isEnabled = status == Status.ACTIVE;

            return userRepository
                    .findAllByActiveIsAndRole_Name(isEnabled, "ROLE_DOCENTE")
                    .stream()
                    .map(user -> ProfessorDTO.builder()
                            .id(user.getIdUser())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .build())
                    .toList();

        }catch (IllegalArgumentException e){
            throw new InvalidArgumentException("El estado ingresado es invalido, estado: <" + status + ">");
        }
    }
}
