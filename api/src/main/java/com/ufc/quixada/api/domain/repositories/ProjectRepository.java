package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project createProject(Project project);
    Project updateProject(Project project);
    List<Project> findAllVisible(Long userId);
    Optional<Project> findByIdIfVisible(Long id, Long userId);

    List<Project> findAllByFreelancerId(Long freelancerId);

    List<Project> findAllByContractorId(Long contractorId);

    boolean userCanViewProject(Long userId, Long projectId);
}
