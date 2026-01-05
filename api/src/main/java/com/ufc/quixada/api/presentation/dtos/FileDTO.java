package com.ufc.quixada.api.presentation.dtos;

public record FileDTO(
        Long id,
        String fileName,
        String fileType,
        String url
) {}

