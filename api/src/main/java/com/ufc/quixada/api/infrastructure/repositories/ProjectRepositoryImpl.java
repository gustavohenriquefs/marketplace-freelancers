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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRepositoryImpl implements ProjectRepository {
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

        ProjectJpaModel projectJpaModel = projectMapper.toJpaEntity(project);

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
        Project domainResult = projectMapper.toDomain(result);

        return domainResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllVisible(Long userId) {
        return jpaRepo.findAllByUserIdIfCanViewProject(userId).stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllByFreelancerId(Long freelancerId) {
        return jpaRepo.findAllByFreelancers_Id(freelancerId).stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findAllByContractorId(Long contractorId) {
        return jpaRepo.findAllByContractor_Id(contractorId).stream()
                .map(projectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findByIdIfVisible(Long id, Long userId) {
        return jpaRepo.findByIdIfIsVisible(id, userId)
                .map(projectMapper::toDomain);
    }

    @Override
    public boolean userCanViewProject(Long userId, Long projectId) {
        return jpaRepo.userCanViewProject(userId, projectId);
    }

    @Override
    @Transactional
    public Project updateProject(Project project) {
        if (project == null || project.getId() == null) {
            throw new IllegalArgumentException("O id do projeto deve ser fornecido para atualização");
        }

        ProjectJpaModel managed = entityManager.find(ProjectJpaModel.class, project.getId());
        if (managed == null) {
            throw new com.ufc.quixada.api.application.exceptions.NotFoundException("Projeto não encontrado");
        }

        // Update status only to avoid unintentionally replacing collections (which may trigger orphanRemoval)
        if (project.getStatus() != null) {
            managed.setStatus(project.getStatus());
        }

        // Add freelancers without touching other collections
        if (project.getFreelancers() != null && !project.getFreelancers().isEmpty()) {
            if (managed.getFreelancers() == null) {
                managed.setFreelancers(new java.util.HashSet<>());
            }
            for (var freelancer : project.getFreelancers()) {
                if (freelancer != null && freelancer.getId() != null) {
                    var freelancerRef = entityManager.getReference(com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel.class, freelancer.getId());
                    managed.getFreelancers().add(freelancerRef);
                }
            }
        }

        ProjectJpaModel updated = jpaRepo.save(managed);
        return projectMapper.toDomain(updated);
    }
}
