package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.presentation.validators.ValueOfEnum;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProjectRequestDTO(
    @NotBlank(message = "O nome do projeto não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    String name,

    @NotBlank(message = "A descrição não pode estar vazia")
    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    String description,

    @NotNull(message = "O orçamento é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O orçamento deve ser maior que 0")
    BigDecimal budget,

    @ValueOfEnum(enumClass =  ProjectStatus.class, nullable = false)
    ProjectStatus status,

    @ValueOfEnum(enumClass =  ExperienceLevel.class, nullable = false)
    ExperienceLevel experienceLevel,

    @NotBlank(message = "O prazo (deadline) não pode estar vazio")
    String deadlineInDays,

    @NotNull(message = "A visibilidade (isPublic) é obrigatória")
    Boolean isPublic,

    @NotNull(message = "O id da categoria é obrigatório")
    Long categoryId,

    @NotNull(message = "O id da subcategoria é obrigatório")
    Long subcategoryId,

    List<MultipartFile> files,

    @NotEmpty(message = "Ao menos uma skill é obrigatória")
    List<@NotNull Long> skillsIds
) {}
