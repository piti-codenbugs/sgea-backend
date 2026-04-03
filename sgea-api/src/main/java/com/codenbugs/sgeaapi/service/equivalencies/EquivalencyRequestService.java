package com.codenbugs.sgeaapi.service.equivalencies;

import com.codenbugs.sgeaapi.controller.equivalencies.CreateEquivalencyRequest;
import com.codenbugs.sgeaapi.dto.equivalencies.EquivalencyRequestDTO;
import com.codenbugs.sgeaapi.dto.equivalencies.ProgramCourseDTO;
import com.codenbugs.sgeaapi.entity.course.Course;
import com.codenbugs.sgeaapi.entity.docente.Professor;
import com.codenbugs.sgeaapi.entity.equivalencies.EquivalencyRequest;
import com.codenbugs.sgeaapi.entity.equivalencies.ProgramCourse;
import com.codenbugs.sgeaapi.entity.student.Student;
import com.codenbugs.sgeaapi.entity.users.SessionHelper;
import com.codenbugs.sgeaapi.enums.equivalencies.EquivalencyStatus;
import com.codenbugs.sgeaapi.exception.CourseDoesNotExistException;
import com.codenbugs.sgeaapi.exception.EquivalencyAlreadyExistsException;
import com.codenbugs.sgeaapi.exception.InvalidArgumentException;
import com.codenbugs.sgeaapi.exception.NotFoundException;
import com.codenbugs.sgeaapi.exception.ProfessorDoesNotExistException;
import com.codenbugs.sgeaapi.exception.StudentDoesNotExistException;
import com.codenbugs.sgeaapi.repository.course.CourseRepository;
import com.codenbugs.sgeaapi.repository.equivalencies.EquivalencyRequestRepository;
import com.codenbugs.sgeaapi.repository.equivalencies.ProgramCourseRepository;
import com.codenbugs.sgeaapi.repository.professor.ProfessorRepository;
import com.codenbugs.sgeaapi.repository.student.StudentRepository;
import com.codenbugs.sgeaapi.service.storage.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EquivalencyRequestService {
    private final EquivalencyRequestRepository equivalencyRequestRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final ProgramCourseRepository programCourseRepository;
    private final SessionHelper sessionHelper;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public List<ProgramCourseDTO> getProgramCoursesByCourseCode(Short courseCode) {
        if (courseCode == null) {
            throw new InvalidArgumentException("El código de curso es obligatorio");
        }

        return programCourseRepository
                .findByProfessorIsNullAndOriginCourseCodeContainingIgnoreCaseOrderByCreatedAtDesc(
                        String.valueOf(courseCode))
                .stream()
                .map(this::mapProgramCourseToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EquivalencyRequestDTO> getMyRequests() {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Student student = studentRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new StudentDoesNotExistException("El usuario autenticado no tiene perfil de estudiante"));

        return equivalencyRequestRepository.findByStudentIdStudentOrderByCreatedAtDesc(student.getIdStudent())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EquivalencyRequestDTO> getPendingRequestsForProfessor() {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Professor professor = professorRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new ProfessorDoesNotExistException("El usuario autenticado no tiene perfil de docente"));

        return equivalencyRequestRepository
                .findByProfessorIdProfessorAndStatusOrderByCreatedAtDesc(
                        professor.getIdProfessor(),
                        EquivalencyStatus.PENDIENTE)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProgramCourseDTO> getPrivateProgramCoursesForProfessor(String originCourseCode) {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Professor professor = professorRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new ProfessorDoesNotExistException("El usuario autenticado no tiene perfil de docente"));

        if (!StringUtils.hasText(originCourseCode)) {
            throw new InvalidArgumentException("El código de curso origen es obligatorio");
        }

        return programCourseRepository
                .findByProfessorIdProfessorAndOriginCourseCodeContainingIgnoreCaseOrderByCreatedAtDesc(
                        professor.getIdProfessor(),
                        originCourseCode.trim())
                .stream()
                .map(this::mapProgramCourseToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquivalencyRequestDTO getRequestByIdForProfessor(Long requestId) {
        EquivalencyRequest request = getRequestAssignedToCurrentProfessor(requestId);
        return mapToDTO(request);
    }

    @Transactional(readOnly = true)
    public EquivalencyRequestDTO getRequestById(Long requestId) {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Student student = studentRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new StudentDoesNotExistException("El usuario autenticado no tiene perfil de estudiante"));

        EquivalencyRequest request = equivalencyRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Solicitud de equivalencia no encontrada"));

        if (!request.getStudent().getIdStudent().equals(student.getIdStudent())) {
            throw new RuntimeException("No tienes permiso para ver esta solicitud");
        }

        return mapToDTO(request);
    }

    @Transactional
    public EquivalencyRequestDTO createRequest(CreateEquivalencyRequest request) {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Student student = studentRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new StudentDoesNotExistException("El usuario autenticado no tiene perfil de estudiante"));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new ProfessorDoesNotExistException("Docente no encontrado"));

        Course destinationCourse = courseRepository.findById(request.getDestinationCourseCode())
                .orElseThrow(() -> new CourseDoesNotExistException("Curso destino no encontrado"));

        boolean alreadyExists = equivalencyRequestRepository
                .existsByStudentIdStudentAndProfessorIdProfessorAndDestinationCourseCodeAndStatus(
                        student.getIdStudent(),
                        professor.getIdProfessor(),
                        destinationCourse.getCode(),
                        EquivalencyStatus.PENDIENTE);

        if (alreadyExists) {
            throw new EquivalencyAlreadyExistsException(
                    "Ya existe una solicitud pendiente para este curso con el docente seleccionado");
        }

        String programUrl;
        String originCourseCode;
        Integer year;
        Integer semester;
        String section;

        if (request.getProgramCourseId() != null) {
            ProgramCourse programCourse = programCourseRepository.findById(request.getProgramCourseId())
                    .orElseThrow(() -> new NotFoundException("Programa de curso no encontrado"));

            programUrl = programCourse.getProgramUrl();
            originCourseCode = programCourse.getOriginCourseCode();
            year = programCourse.getYear();
            semester = programCourse.getSemester();
            section = programCourse.getSection();
        } else {
            validateManualProgramData(request);
            programUrl = uploadFile(request.getProgramFile());
            originCourseCode = request.getOriginCourseCode();
            year = request.getYear();
            semester = request.getSemester();
            section = request.getSection();
        }

        String certificateUrl = uploadFile(request.getCertificateFile());

        EquivalencyRequest entity = EquivalencyRequest.builder()
                .destinationCourse(destinationCourse)
                .student(student)
                .professor(professor)
                .status(EquivalencyStatus.PENDIENTE)
                .programUrl(programUrl)
                .courseCertificateUrl(certificateUrl)
                .originCourseCode(originCourseCode)
                .year(year)
                .semester(semester)
                .section(section)
                .build();

        EquivalencyRequest savedEntity = equivalencyRequestRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Transactional
    public EquivalencyRequestDTO rejectByProfessor(Long requestId, String comment) {
        if (!StringUtils.hasText(comment)) {
            throw new InvalidArgumentException("El motivo de rechazo es obligatorio");
        }

        EquivalencyRequest request = getRequestAssignedToCurrentProfessor(requestId);
        validatePendingStatus(request);

        request.setStatus(EquivalencyStatus.RECHAZADO);
        request.setComment(comment.trim());
        request.setResolutionDate(LocalDateTime.now());

        EquivalencyRequest updated = equivalencyRequestRepository.save(request);
        return mapToDTO(updated);
    }

    @Transactional
    public EquivalencyRequestDTO approveByProfessor(Long requestId, MultipartFile signedProgramFile,
            Long programCourseId) {
        EquivalencyRequest request = getRequestAssignedToCurrentProfessor(requestId);
        validatePendingStatus(request);

        String signedProgramUrl;
        if (programCourseId != null) {
            signedProgramUrl = resolvePrivateProgramCourseUrl(request, programCourseId);
        } else {
            validatePdfFile(signedProgramFile, "El programa firmado en PDF es obligatorio");
            signedProgramUrl = uploadFile(signedProgramFile);
            saveProgramCoursesAfterApproval(request, signedProgramUrl);
        }

        savePublicProgramCourseAfterApproval(request);

        request.setStatus(EquivalencyStatus.ACEPTADO);
        request.setSignedProgramUrl(signedProgramUrl);
        request.setComment(null);
        request.setResolutionDate(LocalDateTime.now());

        EquivalencyRequest updated = equivalencyRequestRepository.save(request);
        return mapToDTO(updated);
    }

    private EquivalencyRequest getRequestAssignedToCurrentProfessor(Long requestId) {
        Long currentUserId = sessionHelper.getCurrentUserId();

        Professor professor = professorRepository.findById(currentUserId)
                .orElseThrow(
                        () -> new ProfessorDoesNotExistException("El usuario autenticado no tiene perfil de docente"));

        EquivalencyRequest request = equivalencyRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Solicitud de equivalencia no encontrada"));

        if (!request.getProfessor().getIdProfessor().equals(professor.getIdProfessor())) {
            throw new NotFoundException("Solicitud de equivalencia no encontrada para este docente");
        }

        return request;
    }

    private void validatePendingStatus(EquivalencyRequest request) {
        if (request.getStatus() != EquivalencyStatus.PENDIENTE) {
            throw new InvalidArgumentException("Solo se pueden revisar solicitudes en estado PENDIENTE");
        }
    }

    private void saveProgramCoursesAfterApproval(EquivalencyRequest request, String signedProgramUrl) {
        String originCourseCode = normalizeOriginCourseCode(request.getOriginCourseCode());

        Integer year = request.getYear();
        Integer semester = request.getSemester();
        String section = request.getSection();

        if (year == null || semester == null || !StringUtils.hasText(section)) {
            throw new InvalidArgumentException("No se puede aprobar: faltan datos del programa del curso");
        }

        boolean existsPrivate = programCourseRepository
                .existsByProfessorIdProfessorAndOriginCourseCodeAndYearAndSemesterAndSection(
                        request.getProfessor().getIdProfessor(),
                        originCourseCode,
                        year,
                        semester,
                        section);

        if (!existsPrivate) {
            programCourseRepository.save(ProgramCourse.builder()
                    .professor(request.getProfessor())
                    .originCourseCode(originCourseCode)
                    .year(year)
                    .semester(semester)
                    .section(section)
                    .programUrl(signedProgramUrl)
                    .build());
        }

    }

    private void savePublicProgramCourseAfterApproval(EquivalencyRequest request) {
        String originCourseCode = normalizeOriginCourseCode(request.getOriginCourseCode());

        Integer year = request.getYear();
        Integer semester = request.getSemester();
        String section = request.getSection();

        if (year == null || semester == null || !StringUtils.hasText(section)) {
            throw new InvalidArgumentException("No se puede aprobar: faltan datos del programa del curso");
        }

        boolean existsPublic = programCourseRepository
                .existsByProfessorIsNullAndOriginCourseCodeAndYearAndSemesterAndSection(
                        originCourseCode,
                        year,
                        semester,
                        section);

        if (!existsPublic) {
            programCourseRepository.save(ProgramCourse.builder()
                    .professor(null)
                    .originCourseCode(originCourseCode)
                    .year(year)
                    .semester(semester)
                    .section(section)
                    .programUrl(request.getProgramUrl())
                    .build());
        }
    }

    private String resolvePrivateProgramCourseUrl(EquivalencyRequest request, Long programCourseId) {
        ProgramCourse privateProgramCourse = programCourseRepository.findById(programCourseId)
                .orElseThrow(() -> new NotFoundException("Programa privado no encontrado"));

        Long professorId = request.getProfessor().getIdProfessor();
        if (privateProgramCourse.getProfessor() == null
                || !professorId.equals(privateProgramCourse.getProfessor().getIdProfessor())) {
            throw new InvalidArgumentException("El programa seleccionado no pertenece al docente autenticado");
        }

        String requestedOriginCourseCode = normalizeOriginCourseCode(request.getOriginCourseCode());
        String privateOriginCourseCode = normalizeOriginCourseCode(privateProgramCourse.getOriginCourseCode());
        if (!privateOriginCourseCode.equalsIgnoreCase(requestedOriginCourseCode)) {
            throw new InvalidArgumentException(
                    "El programa seleccionado no corresponde al curso de origen de la solicitud");
        }

        return privateProgramCourse.getProgramUrl();
    }

    private String normalizeOriginCourseCode(String originCourseCode) {
        if (!StringUtils.hasText(originCourseCode)) {
            throw new InvalidArgumentException("No se puede aprobar: código de curso origen inválido");
        }

        return originCourseCode.trim().toUpperCase();
    }

    private void validatePdfFile(MultipartFile file, String requiredMessage) {
        if (file == null || file.isEmpty()) {
            throw new InvalidArgumentException(requiredMessage);
        }

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        boolean isPdfByContentType = "application/pdf".equalsIgnoreCase(contentType);
        boolean isPdfByExtension = fileName != null && fileName.toLowerCase().endsWith(".pdf");

        if (!isPdfByContentType && !isPdfByExtension) {
            throw new InvalidArgumentException("El archivo debe estar en formato PDF");
        }
    }

    private String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidArgumentException("El archivo es obligatorio");
        }

        try {
            Map<String, String> uploadResult = cloudinaryService.uploadFile(file);
            return uploadResult.get("url");
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a Cloudinary: " + e.getMessage());
        }
    }

    private void validateManualProgramData(CreateEquivalencyRequest request) {
        if (!StringUtils.hasText(request.getOriginCourseCode())) {
            throw new InvalidArgumentException("El código del curso de origen es obligatorio");
        }
        if (request.getYear() == null) {
            throw new InvalidArgumentException("El año es obligatorio");
        }
        if (request.getSemester() == null) {
            throw new InvalidArgumentException("El semestre es obligatorio");
        }
        if (!StringUtils.hasText(request.getSection())) {
            throw new InvalidArgumentException("La sección es obligatoria");
        }
        if (request.getProgramFile() == null || request.getProgramFile().isEmpty()) {
            throw new InvalidArgumentException("Debe subir el programa del curso cuando no existe uno registrado");
        }
    }

    private ProgramCourseDTO mapProgramCourseToDTO(ProgramCourse entity) {
        return ProgramCourseDTO.builder()
                .id(entity.getId())
                .courseCode(entity.getOriginCourseCode())
                .courseName(resolveCourseName(entity.getOriginCourseCode()))
                .professorId(entity.getProfessor() != null ? entity.getProfessor().getIdProfessor() : null)
                .professorName(entity.getProfessor() != null
                        ? entity.getProfessor().getUser().getFirstName() + " "
                                + entity.getProfessor().getUser().getLastName()
                        : "Banco público")
                .year(entity.getYear())
                .semester(entity.getSemester())
                .section(entity.getSection())
                .programUrl(entity.getProgramUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private String resolveCourseName(String originCourseCode) {
        if (!StringUtils.hasText(originCourseCode)) {
            return null;
        }

        String digits = originCourseCode.replaceAll("\\D", "");
        if (!StringUtils.hasText(digits)) {
            return null;
        }

        try {
            Short code = Short.parseShort(digits);
            return courseRepository.findById(code).map(Course::getName).orElse(null);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private EquivalencyRequestDTO mapToDTO(EquivalencyRequest entity) {
        return EquivalencyRequestDTO.builder()
                .id(entity.getId())
                .destinationCourseCode(entity.getDestinationCourse().getCode())
                .destinationCourseName(entity.getDestinationCourse().getName())
                .studentId(entity.getStudent().getIdStudent())
                .studentFullName(entity.getStudent().getUser().getFirstName() + " "
                        + entity.getStudent().getUser().getLastName())
                .professorId(entity.getProfessor().getIdProfessor())
                .professorFullName(entity.getProfessor().getUser().getFirstName() + " "
                        + entity.getProfessor().getUser().getLastName())
                .status(entity.getStatus())
                .comment(entity.getComment())
                .programUrl(entity.getProgramUrl())
                .courseCertificateUrl(entity.getCourseCertificateUrl())
                .signedProgramUrl(entity.getSignedProgramUrl())
                .originCourseCode(entity.getOriginCourseCode())
                .year(entity.getYear())
                .semester(entity.getSemester())
                .section(entity.getSection())
                .createdAt(entity.getCreatedAt())
                .resolutionDate(entity.getResolutionDate())
                .build();
    }
}
