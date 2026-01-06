package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

import java.util.List;

public class GetAllContractorProjects {
    private final ProjectRepository projectRepository;

    public GetAllContractorProjects(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> execute(Long userId) {
        if (userId != null) {
            List<Project> projectsAsContractor = projectRepository.findAllByContractorId(userId);
            if (!projectsAsContractor.isEmpty()) {
                return projectsAsContractor;
            }
        }
        return List.of();
    }
}

