package com.codenbugs.sgeaapi.dto.login;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponseDTO {
    private String token;
    private String message;
    private String name;
    private String email;
    private String role;
}