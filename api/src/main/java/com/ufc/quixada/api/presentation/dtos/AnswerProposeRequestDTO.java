package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.presentation.validators.ValueOfEnum;
import jakarta.validation.constraints.NotNull;

public record AnswerProposeRequestDTO(
        @ValueOfEnum(enumClass = ProposeStatus.class, message = "Status inválido. Use: ACCEPTED, REJECTED, WAITING_ANALYSIS, CANCELED")
        @NotNull(message = "Status é obrigatório")
        String newStatus
) { }