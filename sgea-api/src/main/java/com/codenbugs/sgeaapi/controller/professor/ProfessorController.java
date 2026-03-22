package com.codenbugs.sgeaapi.controller.professor;

import com.codenbugs.sgeaapi.controller.login.RegisterRequest;
import com.codenbugs.sgeaapi.dto.professor.AccountStatusDTO;
import com.codenbugs.sgeaapi.dto.professor.ProfessorDTO;
import com.codenbugs.sgeaapi.entity.users.AccountStatusType;
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
            @RequestParam("status") AccountStatusType status
    ) {
        return ResponseEntity.ok(professorService.getByStatus(status));
    }

    @GetMapping("/{id}")
    public ProfessorDTO getProfessor(
            @PathVariable Long id
    ) {
        return professorService.getById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody RegisterRequest dto
    ){

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> approve(
            @PathVariable Long id,
            @RequestBody AccountStatusDTO  statusDTO
    ){
        professorService.updateAccount(id, statusDTO);
        return ResponseEntity.noContent().build();
    }
}
