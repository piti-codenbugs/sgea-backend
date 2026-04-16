package com.codenbugs.sgeaapi.repository.equivalencies;

import com.codenbugs.sgeaapi.entity.equivalencies.EquivalencyRequest;
import com.codenbugs.sgeaapi.enums.equivalencies.EquivalencyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquivalencyRequestRepository extends JpaRepository<EquivalencyRequest, Long> {
    boolean existsByStudentIdStudentAndProfessorIdProfessorAndDestinationCourseCodeAndStatus(
            Long studentId,
            Long professorId,
            Short destinationCourseCode,
            EquivalencyStatus status);

    List<EquivalencyRequest> findByStudentIdStudentOrderByCreatedAtDesc(Long studentId);

    List<EquivalencyRequest> findByProfessorIdProfessorAndStatusOrderByCreatedAtDesc(
            Long professorId,
            EquivalencyStatus status);
}
