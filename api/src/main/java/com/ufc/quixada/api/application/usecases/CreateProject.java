package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.repositories.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

        // Verify if category exists
        Long categoryId = project.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalArgumentException("Category with ID " + categoryId + " does not exist.")
        );

        // Verify if subcategory exists in the given category
        Long subcategoryId = project.getSubcategory().getId();
        Subcategory subcategory = subRepo.findById(subcategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Subcategory with ID " + subcategoryId + " does not exist."));

        if (!subcategory.getCategory().getId().equals(categoryId)) {
            throw new IllegalArgumentException("Subcategory with ID " + subcategoryId + " does not belong to Category with ID " + categoryId + ".");
        }

        // Verify and fetch skills
        List<Long> skillIds = project.getSkills().stream().map(Skill::getId).collect(Collectors.toList());
        List<Skill> skills = skillRepository.findAllById(skillIds);
        if (skills.isEmpty()) {
            throw new IllegalArgumentException("You must provide at least one valid skill.");
        }

        // Verify and fetch files (if any)
        List<Long> fileIds = project.getFiles().stream()
                .map(File::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<File> files = fileIds.isEmpty() ? java.util.Collections.emptyList() : fileRepository.findAllById(fileIds);

        // NÃO reatribuir as entidades fetched - apenas validar
        // O projeto já tem as referências corretas (com IDs)
        // Hibernate vai gerenciar as associações automaticamente

        // Proceed to create the project
        return this.projectRepository.createProject(project);
    }

}
