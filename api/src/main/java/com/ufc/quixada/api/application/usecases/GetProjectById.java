package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

public class GetProjectById {
    private final ProjectRepository projectRepository;

    public GetProjectById(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project execute(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with id: " + id));
    }
}

