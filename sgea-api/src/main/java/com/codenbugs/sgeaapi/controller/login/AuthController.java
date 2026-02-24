package com.codenbugs.sgeaapi.controller.login;

import com.codenbugs.sgeaapi.dto.login.AuthResponseDTO;
import com.codenbugs.sgeaapi.service.login.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(new AuthResponseDTO());
    }
}
