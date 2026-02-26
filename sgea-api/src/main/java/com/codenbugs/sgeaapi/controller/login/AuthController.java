package com.codenbugs.sgeaapi.controller.login;

import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.entity.login_test.Role;
import com.codenbugs.sgeaapi.entity.login_test.UserTest;
import com.codenbugs.sgeaapi.service.login.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Endpoint para el inicio de sesión.
     *
     * @param request contiene las credenciales del usuario.
     * @return un objeto de tipo ResponseEntity.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Endpoint para el registro de estudiantes.
     *
     * @param request contiene los datos del estudiante.
     * @return un objeto de tipo ResponseEntity.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
