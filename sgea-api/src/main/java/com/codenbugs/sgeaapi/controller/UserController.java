package com.codenbugs.sgeaapi.controller;

import com.codenbugs.sgeaapi.dto.user.UserLoginDTO;
import com.codenbugs.sgeaapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody UserLoginDTO dto
            ){

        return ResponseEntity.noContent().build();
    }
}
