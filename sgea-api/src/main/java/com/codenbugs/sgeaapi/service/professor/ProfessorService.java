package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.enums.Status;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final UserRepository userRepository;

    public List<ProfessorDTO> getByStatus(String statusRequest ) {

        try {
            Status status = Status.valueOf(statusRequest);
            boolean isEnabled = status == Status.ACTIVE;

            List<User> users = userRepository.findAllByActiveIsAndRole_Name( isEnabled, "ROLE_DOCENTE" );
            List<ProfessorDTO> dtos = new ArrayList<>();

            for(User user : users) {
                dtos.add(
                        ProfessorDTO.builder()
                                .id( user.getIdUser() )
                                .firstName( user.getFirstName() )
                                .lastName( user.getLastName() )
                                .email( user.getEmail() )
                                .build()
                );
            }
            return dtos;

        }catch (IllegalArgumentException e){
            throw new InvalidArgumentException("El estado ingresado es invalido, estado: <" + statusRequest + ">");
        }
    }
}
