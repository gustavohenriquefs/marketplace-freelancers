package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;

import java.util.List;

public class GetAllFreelancerProjects {
    private final ProjectRepository projectRepository;

    public GetAllFreelancerProjects(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> execute(Long userId) {
        if (userId != null) {
            List<Project> projectsAsFreelancer = projectRepository.findAllByFreelancerId(userId);
            if (!projectsAsFreelancer.isEmpty()) {
                return projectsAsFreelancer;
            }
        }
        return List.of();
    }
}

