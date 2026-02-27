package com.codenbugs.sgeaapi.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Prueba {
    @PostMapping(value = "/demo")
    public String demo() {
        return "Welcome";
    }
}
