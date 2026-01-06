package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

public class GetProjectById {
    private final ProjectRepository projectRepository;

    public GetProjectById(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project execute(Long id, Long userId) {
        if (!projectRepository.userCanViewProject(userId, id)) {
            throw new NotFoundException("Projeto não encontrado com id: " + id);
        }
        Project project = projectRepository.findByIdIfVisible(id, userId)
                .orElseThrow(() -> new NotFoundException("Projeto não encontrado com id: " + id));
                return project;
    }
}

