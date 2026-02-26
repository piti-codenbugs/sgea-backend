package com.codenbugs.sgeaapi.service;

import com.codenbugs.sgeaapi.controller.login.RegisterRequest;
import com.codenbugs.sgeaapi.entity.login_test.UserTest;
import com.codenbugs.sgeaapi.exception.UserAlreadyExistsException;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import com.codenbugs.sgeaapi.service.login.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {

        // 1️⃣ Arrange (preparar datos)
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");
        request.setFirstName("Michael");
        request.setLastName("Dev");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("encodedPassword");

        when(jwtService.getToken(any(UserTest.class)))
                .thenReturn("fake-jwt");

        // 2️⃣ Act (ejecutar método)
        var response = authService.registerStudent(request);

        // 3️⃣ Assert (verificar resultados)
        assertNotNull(response);
        assertEquals("fake-jwt", response.getToken());

        verify(userRepository, times(1)).save(any(UserTest.class));
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new UserTest()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerStudent(request);
        });

        verify(userRepository, never()).save(any());
    }
}
