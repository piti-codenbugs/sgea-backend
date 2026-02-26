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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthResponseDTO login(LoginRequest request) {
        return null;
    }

    public AuthResponseDTO register(RegisterRequest request) {
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
                .role(Role.STUDENT)
                .build();
    }
}