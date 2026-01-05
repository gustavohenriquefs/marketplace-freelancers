package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

import java.util.List;

public class GetAllProjects {
    private final ProjectRepository projectRepository;

    public GetAllProjects(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> execute() {
        return projectRepository.findAll();
    }
}

