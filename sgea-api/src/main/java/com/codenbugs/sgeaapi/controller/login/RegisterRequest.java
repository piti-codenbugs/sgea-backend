package com.codenbugs.sgeaapi.controller.login;

import com.codenbugs.sgeaapi.entity.login_test.Role;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterRequest {
    String firstName;
    String lastName;
    String email;
    String password;
}