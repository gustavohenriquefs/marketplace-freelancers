package com.ufc.quixada.api.presentation.dtos;

import java.util.List;

public record ProjectResponseDTO(
        Long id,
        List<Long> categoryIds,
        List<Long> subCategoryIds,
        String name,
        String description,
        List<Object> files,
        List<Long> abilitiesId,
        Long experienceLevelId,
        String deadlineDays,
        Boolean isPublic
) {}