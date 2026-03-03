package com.codenbugs.sgeaapi.controller;

import com.codenbugs.sgeaapi.controller.login.AuthController;
import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.security.JwtAuthenticationFilter;
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
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldRegisterStudentSuccessfully() throws Exception {

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("fake-jwt")
                .message("Estudiante creado correctamente")
                .name("Estudiante")
                .email("test@mail.com")
                .role("ROLE_STUDENT")
                .build();

        when(authService.registerStudent(any())).thenReturn(response);

        String json = """
                {
                    "email": "test@mail.com",
                    "password": "1234",
                    "firstName": "Estudiante",
                    "lastName": "Dev",
                    "carnet": "202031123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register-student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("Estudiante"))
                .andExpect(jsonPath("$.role").value("ROLE_STUDENT"));
    }

    @Test
    void shouldRegisterProfessorSuccessfully() throws Exception {

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("fake-jwt")
                .message("Docente creado correctamente")
                .name("Docente")
                .email("test@mail.com")
                .role("ROLE_PROFESSOR")
                .build();

        when(authService.registerProfessor(any())).thenReturn(response);

        String json = """
                {
                    "email": "test@mail.com",
                    "password": "1234",
                    "firstName": "Docente",
                    "lastName": "Dev"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register-professor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.name").value("Docente"))
                .andExpect(jsonPath("$.role").value("ROLE_PROFESSOR"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token("jwt-token")
                .message("Inicio de sesión exitoso")
                .name("Usuario")
                .email("user@mail.com")
                .role("ROLE_STUDENT")
                .build();

        when(authService.login(any())).thenReturn(response);

        String json = """
            {
                "email": "user@mail.com",
                "password": "1234"
            }
            """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("user@mail.com"))
                .andExpect(jsonPath("$.name").value("Usuario"))
                .andExpect(jsonPath("$.role").value("ROLE_STUDENT"));
    }
}