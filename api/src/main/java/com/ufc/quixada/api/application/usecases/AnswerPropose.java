package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;

public class AnswerPropose {

    private final ProposeRepository repository;

    public AnswerPropose(ProposeRepository repository) {
        this.repository = repository;
    }
    
    public void execute(UpdateProposeStatusCommand command) {
        Propose propose = repository.findById(command.proposeId())
                .orElseThrow(() -> new NotFoundException("Propose not found"));

        if (propose.isFinalized()) {
            throw new BusinessException("Propose already finalized");
        }

        propose.updateStatus(command.newStatus());

        repository.save(propose);
    }
}
