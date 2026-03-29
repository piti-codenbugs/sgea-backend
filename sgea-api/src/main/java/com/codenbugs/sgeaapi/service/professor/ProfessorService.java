package com.codenbugs.sgeaapi.service.professor;

import com.codenbugs.sgeaapi.dto.professor.AccountStatusDTO;
import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.dto.professor.UpdateProfessorDTO;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import com.codenbugs.sgeaapi.enums.AccountStatusType;
import com.codenbugs.sgeaapi.entity.users.SessionHelper;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.exception.NotFoundException;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import com.codenbugs.sgeaapi.repository.user.AccountStatusRepository;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import com.codenbugs.sgeaapi.service.email.EmailService;
import com.codenbugs.sgeaapi.service.user.AccountStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionHelper sessionHelper;
    private final EmailService emailService;
    private final AccountStatusRepository accountStatusRepository;
    private final AccountStatusService accountStatusService;

    public List<ProfessorDTO> getByStatus(AccountStatusType status) {
        try {
            return repository
                    .findAllByAccountStatus_Status(status)
                    .stream()
                    .map(professor -> ProfessorDTO.builder()
                            .id(professor.getIdProfessor())
                            .firstName(professor.getUser().getFirstName())
                            .lastName(professor.getUser().getLastName())
                            .email(professor.getUser().getEmail())
                            .registrationDate(professor.getUser().getRegistrationDate())
                            .rejectionReason(accountStatusService.getByProfessor(professor).getComment())
                            .build())
                    .toList();

        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException("El estado ingresado es invalido, estado: <" + status + ">");
        }
    }

    public ProfessorDTO getById(Long id) {
        Professor p = repository.findById(id).orElseThrow(
                () -> new InvalidArgumentException("El professor no existe")
        );

        return ProfessorDTO.builder()
                .id(p.getIdProfessor())
                .firstName(p.getUser().getFirstName())
                .lastName(p.getUser().getLastName())
                .email(p.getUser().getEmail())
                .registrationDate(p.getUser().getRegistrationDate())
                .build();
    }

    public void updateAccount(Long id, AccountStatusDTO statusDTO) {
        Professor p = repository.findById(id).orElseThrow(
                () -> new NotFoundException("El professor no existe")
        );

        AccountStatusType newStatus = statusDTO.getStatus();
        if (newStatus == AccountStatusType.PENDIENTE || newStatus == AccountStatusType.RECHAZADO) {
            p.getUser().setActive(false);
        }

        AccountStatus accountStatus = p.getAccountStatus();
        accountStatus.setStatus(newStatus);
        accountStatus.setAdmin(sessionHelper.getCurrentUser());
        accountStatus.setDate(LocalDateTime.now());
        accountStatus.setComment(statusDTO.getRejectionReason());

        repository.save(p);
        accountStatusRepository.save(accountStatus);

        if (newStatus == AccountStatusType.APROBADO || newStatus == AccountStatusType.RECHAZADO) {
            preparedAndSendEmail(p, newStatus, statusDTO.getRejectionReason());
        }
    }

    /**
     * Método auxiliar para envíar el correo electrónico
     *
     * @param p               es el usuario el cuál recibirá la notificación.
     * @param newStatus       es el estado de su cuenta.
     * @param rejectionReason es la razón por la que se rechazó su cuenta.
     */
    private void preparedAndSendEmail(Professor p, AccountStatusType newStatus, String rejectionReason) {
        boolean isApproved = (newStatus == AccountStatusType.APROBADO);
        String title = isApproved ? "Cuenta de Profesor Aprobada" : "Actualización sobre su cuenta de Profesor";
        String approved = "¡Bienvenido! Tu cuenta ha sido verificada y aprobada por la administración. Ya puedes acceder a todas las funciones.";
        String rejected = "Lamentamos informarte que tu solicitud de cuenta ha sido rechazada.  \nMotivo: " + (rejectionReason != null ? rejectionReason + "\n\n Si ha existido un error comuníquese con administración" : "No especificado.");
        String description = isApproved ? approved : rejected;

        emailService.sendStatusRejectedEmail(p.getUser(), title, description, isApproved);
    }

    public void update(Long id, UpdateProfessorDTO dto) {
        Professor professor = repository.findById(id).orElseThrow(
                () -> new NotFoundException("Docente no encontrado")
        );

        User user = professor.getUser();

        // Validar email único solo si cambió
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new InvalidArgumentException("El correo ya está registrado");
            }
            user.setEmail(dto.getEmail());
        }

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());

        // Solo actualizar contraseña si viene en el request
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repository.save(professor);
    }
}
