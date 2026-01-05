package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.presentation.validators.ValueOfEnum;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para criar projeto via JSON (sem upload de arquivos).
 * Para upload de arquivos, use ProjectRequestDTO com multipart/form-data.
 */
public record CreateProjectJsonDTO(
    @NotBlank(message = "Project name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    String description,

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    BigDecimal budget,

    @ValueOfEnum(enumClass = ProjectStatus.class, nullable = false)
    ProjectStatus status,

    @ValueOfEnum(enumClass = ExperienceLevel.class, nullable = false)
    ExperienceLevel experienceLevel,

    @NotBlank(message = "Deadline is required")
    String deadlineDays,

    @NotNull(message = "Visibility status must be specified")
    Boolean isPublic,

    @NotNull(message = "Category ID is required")
    Long categoryId,

    @NotNull(message = "Sub-category ID is required")
    Long subCategoryId,

    @NotEmpty(message = "At least one skill is required")
    List<@NotNull Long> skillsIds
) {}

