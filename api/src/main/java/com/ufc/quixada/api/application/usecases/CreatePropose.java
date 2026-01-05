package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreatePropose {
    private static final Logger log = LoggerFactory.getLogger(CreatePropose.class);

    private final ProposeRepository proposeRepository;

    public CreatePropose(
            ProposeRepository proposeRepository
    ) {
        this.proposeRepository = proposeRepository;
    }

    public void execute(CreateProposeCommand command) {
        Propose propose = command.propose();
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        Freelancer freelancer = new Freelancer();
        freelancer.setId(command.freelancerId());
        Project project = new Project();
        project.setId(command.projectId());

        if (this.proposeRepository.existsByFreelancerIdAndProjectId(freelancer.getId(), project.getId())) {
            log.error("O freelancer {} já enviou uma proposta para o projeto {}", freelancer.getId(), project.getId());
            throw new BusinessException("O freelancer já enviou uma proposta para este projeto");
        }

        propose.setFreelancer(freelancer);
        propose.setProject(project);

        this.proposeRepository.create(propose);
        System.out.println("✅ Proposta criada com sucesso!");

        log.info("Proposta criada com sucesso");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  ✅ USE CASE: CreatePropose.execute() FINALIZADO           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}