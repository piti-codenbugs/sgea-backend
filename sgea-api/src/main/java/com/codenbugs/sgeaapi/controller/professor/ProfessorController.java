package com.codenbugs.sgeaapi.controller.professor;

import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.entity.users.AccountStatus;
import com.codenbugs.sgeaapi.enums.Status;
import com.codenbugs.sgeaapi.service.professor.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getProfessorsByStatus(
            @RequestParam("status") Status status
    ) {
        return ResponseEntity.ok(professorService.getByStatus(status));
    }

    @GetMapping("/{id}")
    public ProfessorDTO getProfessor(
            @PathVariable Long id
    ) {
        return professorService.getById(id);
    }

    @PatchMapping("" +
            "")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            @PathVariable AccountStatus status
            ){
        professorService.approve(id);
        return ResponseEntity.noContent().build();
    }
}
