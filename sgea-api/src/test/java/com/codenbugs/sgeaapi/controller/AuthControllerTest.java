package com.codenbugs.sgeaapi.controller;

import com.codenbugs.sgeaapi.controller.login.AuthController;
import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.entity.login_test.Role;
import com.codenbugs.sgeaapi.service.jwt.JwtService;
import com.codenbugs.sgeaapi.service.login.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void shouldRegisterStudentSuccessfully() throws Exception {

        // Simulamos respuesta del service
        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("fake-jwt")
                .message("Estudiante registrado correctamente")
                .name("Estudiante")
                .email("test@mail.com")
                .role(Role.STUDENT.name())
                .build();

        when(authService.registerStudent(any())).thenReturn(response);

        // JSON del request
        String json = """
                {
                    "email": "test@mail.com",
                    "password": "1234",
                    "firstName": "Estudiante",
                    "lastName": "Dev"
                }
                """;

        // Ejecutamos el endpoint
        mockMvc.perform(post("/api/v1/auth/register-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("Estudiante"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void shouldRegisterProfessorSuccessfully() throws Exception {

        // Simulamos respuesta del service
        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("fake-jwt")
                .message("Docente registrado correctamente")
                .name("Docente")
                .email("test@mail.com")
                .role(Role.PROFESSOR.name())
                .build();

        when(authService.registerProfessor(any())).thenReturn(response);

        // JSON del request
        String json = """
                {
                    "email": "test@mail.com",
                    "password": "1234",
                    "firstName": "Docente",
                    "lastName": "Dev"
                }
                """;

        // Ejecutamos el endpoint
        mockMvc.perform(post("/api/v1/auth/register-professor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("Docente"))
                .andExpect(jsonPath("$.role").value("PROFESSOR"));
    }

    @Test
    void shouldLoginProfessorSuccessfully() throws Exception {

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("professor-jwt")
                .message("Login exitoso")
                .name("Profesor")
                .email("professor@mail.com")
                .role(Role.PROFESSOR.name())
                .build();

        when(authService.login(any())).thenReturn(response);

        String json = """
            {
                "email": "professor@mail.com",
                "password": "1234"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("professor-jwt"))
                .andExpect(jsonPath("$.email").value("professor@mail.com"))
                .andExpect(jsonPath("$.name").value("Profesor"))
                .andExpect(jsonPath("$.role").value("PROFESSOR"));
    }

    @Test
    void shouldLoginStudentSuccessfully() throws Exception {

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("student-jwt")
                .message("Login exitoso")
                .name("Estudiante")
                .email("student@mail.com")
                .role(Role.STUDENT.name())
                .build();

        when(authService.login(any())).thenReturn(response);

        String json = """
            {
                "email": "student@mail.com",
                "password": "1234"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("student-jwt"))
                .andExpect(jsonPath("$.email").value("student@mail.com"))
                .andExpect(jsonPath("$.name").value("Estudiante"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }
}