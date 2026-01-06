package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO de resposta para Project.
 * Não expõe entidades de domínio diretamente, apenas DTOs simples.
 */
public record ProjectResponseDTO(
        Long id,
        String name,
        String description,
        BigDecimal budget,
        ProjectStatus status,
        ExperienceLevel experienceLevel,
        Long deadlineInDays,
        Boolean isPublic,
        LocalDate createdAt,
        LocalDate updatedAt,
        CategoryResponseDTO category,
        SubcategoryResponseDTO subcategory,
        ContractorResponseDTO contractor,
        List<ProposeResponseDTO> proposes,
        List<FileResponseDTO> files,
        List<SkillResponseDTO> skills
) {}

