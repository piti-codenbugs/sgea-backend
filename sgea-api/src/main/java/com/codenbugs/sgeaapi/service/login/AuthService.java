package com.codenbugs.sgeaapi.service.login;

import com.codenbugs.sgeaapi.controller.login.LoginRequest;
import com.codenbugs.sgeaapi.controller.login.RegisterRequest;
import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.student.Student;
import com.codenbugs.sgeaapi.entity.users.Role;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.exception.UserAlreadyExistsException;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import com.codenbugs.sgeaapi.repository.student.StudentRepository;
import com.codenbugs.sgeaapi.repository.user.RoleRepository;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    public AuthResponseDTO login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseDTO.builder()
                .token(token)
                .message("Inicio de sesión exitoso")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    @Transactional
    public AuthResponseDTO registerStudent(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(studentRole)
                .active(true)
                .registrationDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        Student student = Student.builder()
                .carnet(request.getCarnet())
                .user(user)
                .build();

        studentRepository.save(student);
        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .message("Estudiante creado correctamente")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }

    @Transactional
    public AuthResponseDTO registerProfessor(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }

        Role professorRole = roleRepository.findByName("ROLE_PROFESSOR")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(professorRole)
                .active(true)
                .registrationDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        Professor professor = Professor.builder()
                .user(user)
                .build();

        professorRepository.save(professor);
        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .message("Docente creado correctamente")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}