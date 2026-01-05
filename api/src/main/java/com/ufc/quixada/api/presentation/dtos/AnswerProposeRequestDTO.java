package com.ufc.quixada.api.presentation.dtos;

import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.presentation.validators.ValueOfEnum;
import jakarta.validation.constraints.NotNull;

public record AnswerProposeRequestDTO(
        @ValueOfEnum(enumClass = ProposeStatus.class)
        @NotNull
        ProposeStatus newStatus
) { }