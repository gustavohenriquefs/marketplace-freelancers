package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;

public class IssuePropose {
    private final ProposeRepository proposeRepository;

    public IssuePropose(ProposeRepository proposeRepository) {
        this.proposeRepository = proposeRepository;
    }

    public Propose execute(Propose propose) {
        return proposeRepository.create(propose);
    }
}
