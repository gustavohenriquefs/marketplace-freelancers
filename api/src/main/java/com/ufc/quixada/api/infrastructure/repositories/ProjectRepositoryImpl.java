package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.infrastructure.models.CategoryJpaModel;
import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.SkillJpaModel;
import com.ufc.quixada.api.infrastructure.models.SubcategoryJpaModel;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    public ProjectRepositoryImpl(JpaProjectRepository jpaRepo, ProjectMapper projectMapper, EntityManager entityManager) {
        this.jpaRepo = jpaRepo;
        this.projectMapper = projectMapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Project createProject(Project project) {
        log.info("=== Salvando projeto no banco ===");
        log.info("Projeto recebido: {}", project);

        ProjectJpaModel projectJpaModel = projectMapper.toJpaEntity(project);
        log.info("Projeto convertido para JPA: {}", projectJpaModel);

        // Garante que relações (FK/N:N) apontem para entidades gerenciadas
        // evitando erros do tipo "detached entity passed to persist".
        if (projectJpaModel.getContractor() != null && projectJpaModel.getContractor().getId() != null) {
            projectJpaModel.setContractor(
                entityManager.getReference(ContractorJpaModel.class, projectJpaModel.getContractor().getId())
            );
        }

        if (projectJpaModel.getCategory() != null && projectJpaModel.getCategory().getId() != null) {
            projectJpaModel.setCategory(
                entityManager.getReference(CategoryJpaModel.class, projectJpaModel.getCategory().getId())
            );
        }

        if (projectJpaModel.getSubcategory() != null && projectJpaModel.getSubcategory().getId() != null) {
            projectJpaModel.setSubcategory(
                entityManager.getReference(SubcategoryJpaModel.class, projectJpaModel.getSubcategory().getId())
            );
        }

        if (projectJpaModel.getSkills() != null && !projectJpaModel.getSkills().isEmpty()) {
            List<SkillJpaModel> managedSkills = projectJpaModel.getSkills().stream()
                .filter(s -> s != null && s.getId() != null)
                .map(s -> entityManager.getReference(SkillJpaModel.class, s.getId()))
                .toList();
            projectJpaModel.setSkills(managedSkills);
        }

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
