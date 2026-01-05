package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnswerPropose {
    private static final Logger log = LoggerFactory.getLogger(AnswerPropose.class);

    private final ProposeRepository repository;

    public AnswerPropose(ProposeRepository repository) {
        this.repository = repository;
    }
    
    public void execute(UpdateProposeStatusCommand command) {

        log.info("Buscando proposta ID: {}", command.proposeId());
        System.out.println("🔍 Buscando proposta no banco com ID: " + command.proposeId());

        Propose propose = repository.findById(command.proposeId())
                .orElseThrow(() -> new NotFoundException("Propose not found"));

        System.out.println("✅ Proposta encontrada: " + propose);
        log.info("Proposta encontrada: {}", propose);

        if (propose.isFinalized()) {
            throw new BusinessException("Propose already finalized");
        }

        System.out.println("🔄 Atualizando status para: " + command.newStatus());
        propose.updateStatus(command.newStatus());
        System.out.println("✅ Status atualizado na entidade");

        System.out.println("💾 Salvando proposta no banco...");
        repository.save(propose);
        System.out.println("✅ Proposta salva com sucesso!");

        log.info("Proposta atualizada e salva com sucesso");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  ✅ USE CASE: AnswerPropose.execute() FINALIZADO          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
