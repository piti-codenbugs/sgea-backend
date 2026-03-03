package com.codenbugs.sgeaapi.service;

import com.codenbugs.sgeaapi.controller.login.RegisterRequest;
import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.entity.student.Student;
import com.codenbugs.sgeaapi.entity.users.Role;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.exception.UserAlreadyExistsException;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import com.codenbugs.sgeaapi.repository.student.StudentRepository;
import com.codenbugs.sgeaapi.repository.user.RoleRepository;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import com.codenbugs.sgeaapi.service.login.AuthService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterStudentSuccessfully() {

        RegisterRequest request = RegisterRequest.builder()
                .email("test@mail.com")
                .password("1234")
                .firstName("Michael")
                .lastName("Dev")
                .carnet("202031123")
                .build();

        Role role = new Role();
        role.setName("ROLE_STUDENT");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName("ROLE_STUDENT"))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode("1234"))
                .thenReturn("encodedPassword");

        when(jwtService.getToken(any(User.class)))
                .thenReturn("fake-jwt");

        AuthResponseDTO response = authService.registerStudent(request);

        assertNotNull(response);
        assertEquals("fake-jwt", response.getToken());
        assertEquals("ROLE_STUDENT", response.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyExists() {

        RegisterRequest request = RegisterRequest.builder()
                .email("test@mail.com")
                .password("1234")
                .firstName("Michael")
                .lastName("Dev")
                .carnet("202031123")
                .build();

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () ->
                authService.registerStudent(request)
        );

        verify(userRepository, never()).save(any());
        verify(studentRepository, never()).save(any());
    }
}