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

        return programCourseRepository.findByCourseCodeOrderByCreatedAtDesc(courseCode)
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
            originCourseCode = String.valueOf(programCourse.getCourse().getCode());
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
                .courseCode(entity.getCourse().getCode())
                .courseName(entity.getCourse().getName())
                .professorId(entity.getProfessor().getIdProfessor())
                .professorName(entity.getProfessor().getUser().getFirstName() + " "
                        + entity.getProfessor().getUser().getLastName())
                .year(entity.getYear())
                .semester(entity.getSemester())
                .section(entity.getSection())
                .programUrl(entity.getProgramUrl())
                .build();
    }

    private EquivalencyRequestDTO mapToDTO(EquivalencyRequest entity) {
        return EquivalencyRequestDTO.builder()
                .id(entity.getId())
                .destinationCourseCode(entity.getDestinationCourse().getCode())
            .destinationCourseName(entity.getDestinationCourse().getName())
                .studentId(entity.getStudent().getIdStudent())
                .professorId(entity.getProfessor().getIdProfessor())
            .professorFullName(entity.getProfessor().getUser().getFirstName() + " "
                + entity.getProfessor().getUser().getLastName())
                .status(entity.getStatus())
                .programUrl(entity.getProgramUrl())
                .courseCertificateUrl(entity.getCourseCertificateUrl())
                .originCourseCode(entity.getOriginCourseCode())
                .year(entity.getYear())
                .semester(entity.getSemester())
                .section(entity.getSection())
                .createdAt(entity.getCreatedAt())
            .resolutionDate(entity.getResolutionDate())
                .build();
    }
}
