package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.presentation.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
        }
)
public interface ProjectMapper {

    // ===== Mapeamento de DTO de entrada para Domain =====
    
    @Mapping(target = "files", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "mapCategory")
    @Mapping(target = "subcategory", source = "subcategoryId", qualifiedByName = "mapSubcategory")
    @Mapping(target = "skills", source = "skillsIds", qualifiedByName = "mapSkills")
    @Mapping(target = "contractor", ignore = true)
    @Mapping(target = "proposes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Project toDomain(CreateProjectJsonDTO dto);

    // ===== Mapeamento de Domain para DTO de resposta =====
    
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToDTO")
    @Mapping(target = "subcategory", source = "subcategory", qualifiedByName = "subcategoryToDTO")
    @Mapping(target = "contractor", source = "contractor", qualifiedByName = "contractorToDTO")
    @Mapping(target = "files", source = "files", qualifiedByName = "filesToDTO")
    @Mapping(target = "skills", source = "skills", qualifiedByName = "skillsToDTO")
    ProjectResponseDTO toDto(Project project);

    // ===== Mapeamento entre Domain e JPA =====
    
    @Mapping(target = "proposes", ignore = true)
    @Mapping(target = "files", ignore = true)
    ProjectJpaModel toJpaEntity(Project project);

    @Mapping(target = "proposes", ignore = true)
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
    default CategoryDTO categoryToDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(category.getId(), category.getName());
    }

    @Named("subcategoryToDTO")
    default SubcategoryDTO subcategoryToDTO(Subcategory subcategory) {
        if (subcategory == null) {
            return null;
        }
        return new SubcategoryDTO(
                subcategory.getId(),
                subcategory.getName(),
                subcategory.getCategory() != null ? subcategory.getCategory().getId() : null
        );
    }

    @Named("contractorToDTO")
    default ContractorDTO contractorToDTO(Contractor contractor) {
        if (contractor == null || contractor.getUser() == null) {
            return null;
        }
        return new ContractorDTO(
                contractor.getId(),
                contractor.getUser().getName(),
                contractor.getUser().getEmail()
        );
    }

    @Named("filesToDTO")
    default List<FileDTO> filesToDTO(List<File> files) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        return files.stream()
                .map(file -> new FileDTO(
                        file.getId(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getUrl()
                ))
                .toList();
    }

    @Named("skillsToDTO")
    default List<SkillDTO> skillsToDTO(List<Skill> skills) {
        if (skills == null || skills.isEmpty()) {
            return Collections.emptyList();
        }
        return skills.stream()
                .map(skill -> new SkillDTO(skill.getId(), skill.getName()))
                .toList();
    }
}
