package com.codenbugs.sgeaapi.controller.equivalencies;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CreateEquivalencyRequest {
    @NotNull(message = "El curso destino es obligatorio")
    private Short destinationCourseCode;

    @NotNull(message = "El docente es obligatorio")
    private Long professorId;

    private MultipartFile programFile;

    private Long programCourseId;

    @NotNull(message = "El archivo de constancia de cursos es obligatorio")
    private MultipartFile certificateFile;

    private String originCourseCode;
    private Integer year;
    private Integer semester;
    private String section;
}
