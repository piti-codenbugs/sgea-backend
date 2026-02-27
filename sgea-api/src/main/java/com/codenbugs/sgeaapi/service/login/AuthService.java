package com.codenbugs.sgeaapi.service.login;

import com.codenbugs.sgeaapi.controller.login.LoginRequest;
import com.codenbugs.sgeaapi.controller.login.RegisterRequest;
import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.entity.login_test.Role;
import com.codenbugs.sgeaapi.entity.login_test.UserTest;
import com.codenbugs.sgeaapi.exception.UserAlreadyExistsException;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthResponseDTO login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserTest user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponseDTO.builder()
                .token(token)
                .message("Inicio de sesión exitoso")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    public AuthResponseDTO registerStudent(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }

        UserTest user = UserTest.builder()
                .userName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .build();

        userRepository.save(user);
        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .message("Estudiante creado correctamente")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(Role.STUDENT.name())
                .build();
    }

    public AuthResponseDTO registerProfessor(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("El usuario ya está registrado");
        }

        UserTest user = UserTest.builder()
                .userName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROFESSOR)
                .build();

        userRepository.save(user);
        return AuthResponseDTO.builder()
                .token(jwtService.getToken(user))
                .message("Docente creado correctamente")
                .name(user.getUsername())
                .email(user.getEmail())
                .role(Role.PROFESSOR.name())
                .build();
    }
}