package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.presentation.validators.ValueOfEnum;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProjectRequestDTO(

    @NotBlank(message = "Project name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    String name,

    @NotBlank(message = "Description is required")
    @Size(max = 2000)
    String description,

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    BigDecimal budget,

    @ValueOfEnum(enumClass =  ProjectStatus.class, nullable = false)
    ProjectStatus status,

    @ValueOfEnum(enumClass =  ExperienceLevel.class, nullable = false)
    ExperienceLevel experienceLevel,

    @NotBlank(message = "Deadline is required")
    String deadlineDays,

    @NotNull(message = "Visibility status must be specified")
    Boolean isPublic,

    @NotNull(message = "Category ID is required")
    Long categoryId,

    @NotNull(message = "Sub-category ID is required")
    Long subCategoryId,

    // Validates each item inside the list if they were DTOs (using @Valid)
    // @NotEmpty ensures the list itself isn't empty
    List<MultipartFile> files,

    @NotEmpty(message = "At least one ability is required")
    List<@NotNull Long> skillsIds
) {}
