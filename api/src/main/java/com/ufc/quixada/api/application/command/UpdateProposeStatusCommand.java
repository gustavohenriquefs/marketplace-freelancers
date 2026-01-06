package com.ufc.quixada.api.application.command;

import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.enums.ProposeStatus;

public record UpdateProposeStatusCommand(
        Long proposeId,
        ProposeStatus newStatus,
        User user
) {}

