package com.codenbugs.sgeaapi.controller.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "No es un correo electrónico válido")
    private String email;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    private String carnet;
}