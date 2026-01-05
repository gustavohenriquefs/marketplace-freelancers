package com.ufc.quixada.api.application.command;

import com.ufc.quixada.api.domain.entities.Propose;

public record CreateProposeCommand(
    Propose propose,
    Long freelancerId,
    Long projectId
) {
}
