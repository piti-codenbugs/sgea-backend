package com.codenbugs.sgeaapi.controller.login;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    String email;
    String password;
}