package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.repositories.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
public class CreateProject {
    protected final FileRepository fileRepository;
    protected final SubcategoryRepository subRepo;
    protected final CategoryRepository categoryRepository;
    protected final ProjectRepository projectRepository;
    protected final SkillRepository skillRepository;
    protected final ContractorRepository contractorRepository;

    public CreateProject(
            FileRepository fileRepository,
            SubcategoryRepository subRepo,
            CategoryRepository categoryRepository,
            ProjectRepository projectRepository,
            SkillRepository skillRepository,
            ContractorRepository contractorRepository
    ) {
        this.fileRepository = fileRepository;
        this.subRepo = subRepo;
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.contractorRepository = contractorRepository;
    }

    public Project execute(Project project, Long contratodId) {
        Contractor contractor = contractorRepository.findById(contratodId).orElseThrow(
                () -> new IllegalArgumentException("Contractor with ID " + contratodId + " does not exist.")
        );
        project.setContractor(contractor);

        Long categoryId = project.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Category with ID " + categoryId + " does not exist.")
        );

        Long subcategoryId = project.getSubcategory().getId();
        Subcategory subcategory = subRepo.findById(subcategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory with ID " + subcategoryId + " does not exist."));

        if (!subcategory.getCategory().getId().equals(category.getId())) {
            throw new IllegalArgumentException("Subcategory with ID " + subcategoryId + " does not belong to Category with ID " + categoryId + ".");
        }

        List<Long> skillIds = project.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
        List<Skill> skills = skillRepository.findAllById(skillIds);
        if (skills.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one valid skill.");
        }
    
        
        return this.projectRepository.createProject(project);
    }

}
