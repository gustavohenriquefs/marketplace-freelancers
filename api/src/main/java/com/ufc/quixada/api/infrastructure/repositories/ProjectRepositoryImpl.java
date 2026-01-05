package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRepositoryImpl implements ProjectRepository {
    private static final Logger log = LoggerFactory.getLogger(ProjectRepositoryImpl.class);

    private final JpaProjectRepository jpaRepo;
    private final ProjectMapper projectMapper;

    public ProjectRepositoryImpl(JpaProjectRepository jpaRepo, ProjectMapper projectMapper) {
        this.jpaRepo = jpaRepo;
        this.projectMapper = projectMapper;
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        log.info("=== Salvando projeto no banco ===");
        log.info("Projeto recebido: {}", project);

        //TODO: Fazer o parse das entidades dentro de projeto que vão ser criadas ou carregadas(files, skills, category, subcategory, etc)
        ProjectJpaModel projectJpaModel = projectMapper.toJpaEntity(project);
        log.info("Projeto convertido para JPA: {}", projectJpaModel);

        ProjectJpaModel result = this.jpaRepo.save(projectJpaModel);
        log.info("Projeto salvo com ID: {}", result != null ? result.getId() : "null");

        Project domainResult = projectMapper.toDomain(result);
        log.info("Projeto convertido de volta para domínio: {}", domainResult);

        return domainResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return jpaRepo.findAll().stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findById(Long id) {
        return jpaRepo.findById(id)
                .map(projectMapper::toDomain);
    }
}
