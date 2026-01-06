package com.ufc.quixada.api.application.command;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.entities.User;

public record CreateProposeCommand(
    Propose propose,
    User user,
    Long projectId
) {
}
