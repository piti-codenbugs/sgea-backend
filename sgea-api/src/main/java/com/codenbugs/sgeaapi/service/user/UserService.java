package com.codenbugs.sgeaapi.service.user;

import java.util.HashMap;
import java.util.Map;
import com.codenbugs.sgeaapi.entity.users.User;
import com.codenbugs.sgeaapi.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> updateProfile(User currentUser, Map<String, String> body) {
        User dbUser = userRepository.findById(currentUser.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        if (body.containsKey("firstName") && !body.get("firstName").isBlank()) {
            dbUser.setFirstName(body.get("firstName"));
        }
        if (body.containsKey("lastName") && !body.get("lastName").isBlank()) {
            dbUser.setLastName(body.get("lastName"));
        }
        if (body.containsKey("password") && !body.get("password").isBlank()) {
            if (!body.containsKey("currentPassword") || body.get("currentPassword").isBlank()) {
                throw new RuntimeException("Debes ingresar tu contraseña actual");
            }
            if (!passwordEncoder.matches(body.get("currentPassword"), dbUser.getPassword())) {
                throw new IllegalArgumentException("La contraseña actual es incorrecta");
            }
            dbUser.setPassword(passwordEncoder.encode(body.get("password")));
        }

        userRepository.save(dbUser);
        Map<String, String> response = new HashMap<>();
        response.put("firstName", dbUser.getFirstName());
        response.put("lastName", dbUser.getLastName());
        response.put("email", dbUser.getEmail());

        return response;
    }

    public Map<String, String> getProfile(User currentUser) {
        User dbUser = userRepository.findById(currentUser.getIdUser())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, String> response = new HashMap<>();
        response.put("firstName", dbUser.getFirstName());
        response.put("lastName", dbUser.getLastName());
        response.put("email", dbUser.getEmail());
        return response;
    }
}