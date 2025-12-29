package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

public class CreateProject {
    protected final ProjectRepository projectRepository;

    public CreateProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project  execute(Project project) {
        return this.projectRepository.createProject(project);
    }

}
