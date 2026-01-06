package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaProjectRepository extends JpaRepository<ProjectJpaModel, Long> {
    // Strict methods: only projects where the user is related (no public fallback)
    List<ProjectJpaModel> findAllByFreelancers_Id(Long freelancerId);

    List<ProjectJpaModel> findAllByContractor_Id(Long contractorId);

    @Query(
        "SELECT p " +
        "FROM ProjectJpaModel p " +
        "WHERE p.id = :id AND (p.isPublic = true OR p.contractor.user.id = :userId OR EXISTS (" +
        "   SELECT f " +
        "   FROM p.freelancers f " +
        "   WHERE f.user.id = :userId" +
        "))"
    )
    Optional<ProjectJpaModel> findByIdIfIsVisible(Long id, Long userId);

    // Convenience methods that also include public projects
    @Query(
        "SELECT p " +
        "FROM ProjectJpaModel p " +
        "LEFT JOIN p.contractor c " +
        "LEFT JOIN p.freelancers f " +
        "WHERE p.isPublic = true OR c.user.id = :userId OR f.user.id = :userId"
    )
    List<ProjectJpaModel> findAllByUserIdIfCanViewProject(Long userId);

    @Query(
        "SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
        "FROM ProjectJpaModel p " +
        "LEFT JOIN p.contractor c " +
        "LEFT JOIN p.freelancers f " +
        "WHERE p.id = :projectId AND (p.isPublic = true OR c.user.id = :userId OR f.user.id = :userId)"
    )
    boolean userCanViewProject(Long userId, Long projectId);
}