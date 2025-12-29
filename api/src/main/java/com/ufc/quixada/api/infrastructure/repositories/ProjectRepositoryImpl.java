package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaEntity;

public class ProjectRepositoryImpl implements ProjectRepository {
    private final JpaProjectRepository jpaRepo;
    private final ProjectMapper projectMapper;

    public ProjectRepositoryImpl(JpaProjectRepository jpaRepo, ProjectMapper projectMapper) {
        this.jpaRepo = jpaRepo;
        this.projectMapper = projectMapper;
    }

    @Override
    public Project createProject(Project project) {
        //TODO: Fazer o parse das entidades dentro de projeto que vão ser criadas ou carregadas(files, skills, category, subcategory, etc)
        ProjectJpaEntity projectJpaEntity = projectMapper.toJpaEntity(project);
        ProjectJpaEntity result = this.jpaRepo.save(projectJpaEntity);

        return projectMapper.toDomain(result);

    }
}
