package com.ufc.quixada.api.presentation.dtos;

public record FileResponseDTO(
        Long id,
        String fileName,
        String fileType,
        String url
) {}

