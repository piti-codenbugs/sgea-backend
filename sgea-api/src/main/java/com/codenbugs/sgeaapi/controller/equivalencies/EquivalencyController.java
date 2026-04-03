package com.codenbugs.sgeaapi.controller.equivalencies;

import com.codenbugs.sgeaapi.dto.equivalencies.EquivalencyRequestDTO;
import com.codenbugs.sgeaapi.dto.equivalencies.ProgramCourseDTO;
import com.codenbugs.sgeaapi.service.equivalencies.EquivalencyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equivalencias")
@RequiredArgsConstructor
public class EquivalencyController {
    private final EquivalencyRequestService equivalencyRequestService;

    @GetMapping("/student")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<List<EquivalencyRequestDTO>> getMyRequests() {
        return ResponseEntity.ok(equivalencyRequestService.getMyRequests());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<EquivalencyRequestDTO> getRequestById(@PathVariable("id") Long requestId) {
        return ResponseEntity.ok(equivalencyRequestService.getRequestById(requestId));
    }

    @GetMapping("/programas-curso")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<List<ProgramCourseDTO>> getProgramCoursesByCourseCode(
            @RequestParam("courseCode") Short courseCode) {
        return ResponseEntity.ok(equivalencyRequestService.getProgramCoursesByCourseCode(courseCode));
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<EquivalencyRequestDTO> create(
            @RequestParam("destinationCourseCode") Short destinationCourseCode,
            @RequestParam("professorId") Long professorId,
            @RequestParam(value = "programFile", required = false) MultipartFile programFile,
            @RequestParam(value = "programCourseId", required = false) Long programCourseId,
            @RequestParam("certificateFile") MultipartFile certificateFile,
            @RequestParam(value = "originCourseCode", required = false) String originCourseCode,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "semester", required = false) Integer semester,
            @RequestParam(value = "section", required = false) String section) {

        CreateEquivalencyRequest request = CreateEquivalencyRequest.builder()
                .destinationCourseCode(destinationCourseCode)
                .professorId(professorId)
                .programFile(programFile)
                .programCourseId(programCourseId)
                .certificateFile(certificateFile)
                .originCourseCode(originCourseCode)
                .year(year)
                .semester(semester)
                .section(section)
                .build();

        return new ResponseEntity<>(equivalencyRequestService.createRequest(request), HttpStatus.CREATED);
    }
}
