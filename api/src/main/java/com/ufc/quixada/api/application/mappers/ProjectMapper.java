package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.presentation.dtos.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ContractorMapper.class,
                SubcategoryMapper.class,
                CategoryMapper.class,
                ProposeMapper.class,
                SkillMapper.class,
                FileMapper.class,
        }
)
public interface ProjectMapper {

    // ===== Mapeamento de DTO de entrada para Domain =====
    
    @Mapping(target = "files", source = "files", qualifiedByName = "mockFilesDTOToDomain")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "subcategory", source = "subcategoryId", qualifiedByName = "mapSubcategory")
    @Mapping(target = "skills", source = "skillsIds", qualifiedByName = "mapSkills")
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "proposes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project toDomain(CreateProjectRequestDTO dto);

    // ===== Mapeamento de Domain para DTO de resposta =====
    
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToDTO")
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "subcategoryToDTO")
    @Mapping(target = "contractor", source = "contractor", qualifiedByName = "contractorToDTO")
    @Mapping(target = "files", source = "files", qualifiedByName = "filesToDTO")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "skillsToDTO")
    @Mapping(target = "proposes", source = "proposes", qualifiedByName = "proposesToDTO")
    ProjectResponseDTO toDto(Project project);

    // ===== Mapeamento entre Domain e JPA =====
    
    ProjectJpaModel toJpaEntity(Project project);

    @AfterMapping
    default void attachFilesToProject(@MappingTarget ProjectJpaModel projectJpaModel) {
        if (projectJpaModel.getFiles() == null || projectJpaModel.getFiles().isEmpty()) {
            return;
        }

        for (var file : projectJpaModel.getFiles()) {
            if (file != null) {
                file.setProject(projectJpaModel);
            }
        }
    }

    @AfterMapping
    default void attachProposesToProject(@MappingTarget ProjectJpaModel projectJpaModel) {
        if (projectJpaModel.getProposes() == null || projectJpaModel.getProposes().isEmpty()) {
            return;
        }

        for (var propose : projectJpaModel.getProposes()) {
            if (propose != null) {
                propose.setProject(projectJpaModel);
            }
        }
    }

    @Mapping(target = "contractor.user.freelancerProfile", ignore = true)
    @Mapping(target = "contractor.user.contractorProfile", ignore = true)
    @Mapping(target = "contractor.projects", ignore = true)
    Project toDomain(ProjectJpaModel jpaEntity);

    // ===== Métodos auxiliares para criação (ID → Domain Entity) =====
    
    @Named("mapCategory")
    default Category mapCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return new Category(categoryId, null);
    }

    @Named("mapSubcategory")
    default Subcategory mapSubcategory(Long subcategoryId) {
        if (subcategoryId == null) {
            return null;
        }
        return new Subcategory(subcategoryId, null, null);
    }

    @Named("mapSkills")
    default List<Skill> mapSkills(List<Long> skillsIds) {
        if (skillsIds == null || skillsIds.isEmpty()) {
            return Collections.emptyList();
        }
        return skillsIds.stream()
                .map(id -> new Skill(id, null))
                .toList();
    }

    // ===== Métodos auxiliares para resposta (Domain Entity → DTO) =====

    @Named("categoryToDTO")
    default CategoryResponseDTO categoryToDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    @Named("subcategoryToDTO")
    default SubcategoryResponseDTO subcategoryToDTO(Subcategory subcategory) {
        if (subcategory == null) {
            return null;
        }
        return new SubcategoryResponseDTO(
                subcategory.getId(),
                subcategory.getName(),
                subcategory.getCategory() != null ? subcategory.getCategory().getId() : null
        );
    }

    @Named("contractorToDTO")
    default ContractorResponseDTO contractorToDTO(Contractor contractor) {
        if (contractor == null || contractor.getUser() == null) {
            return null;
        }
        return new ContractorResponseDTO(
                contractor.getId(),
                contractor.getUser().getName(),
                contractor.getUser().getEmail()
        );
    }

    @Named("filesToDTO")
    default List<FileResponseDTO> filesToDTO(List<File> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        return files.stream()
                .map(file -> new FileResponseDTO(
                        file.getId(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getUrl()
                ))
                .toList();
    }

    @Named("skillsToDTO")
    default List<SkillResponseDTO> skillsToDTO(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptyList();
        }
        return skills.stream()
                .map(skill -> new SkillResponseDTO(skill.getId(), skill.getName()))
                .toList();
    }

    @Named("proposesToDTO")
    default List<ProposeResponseDTO> proposesToDTO(List<Propose> proposes) {
        if (proposes == null || proposes.isEmpty()) {
            return Collections.emptyList();
        }
        return proposes.stream()
                .map(propose -> {
                    Long freelancerId = propose.getFreelancer() != null ? propose.getFreelancer().getId() : null;
                    return new ProposeResponseDTO(
                            propose.getId(),
                            propose.getStatus(),
                            propose.getPrice(),
                            propose.getDuration(),
                            propose.getDescription(),
                            propose.getCreatedAt(),
                            propose.getUpdatedAt(),
                            freelancerId
                    );
                })
                .toList();
    }

    @Named("mockFilesDTOToDomain")
    default List<File> mockFilesDTOToDomain(List<String> filesDTO) {
        if (filesDTO == null || filesDTO.isEmpty()) {
            return Collections.emptyList();
        }
        return filesDTO.stream()
                .map(fileName -> new File(null, fileName, "pdf", "https://pdfobject.com/pdf/sample.pdf")).toList();
    }
}
