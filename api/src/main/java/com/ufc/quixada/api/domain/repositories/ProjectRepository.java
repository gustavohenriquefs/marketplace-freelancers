package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project createProject(Project project);
    List<Project> findAll();
    Optional<Project> findById(Long id);
}
