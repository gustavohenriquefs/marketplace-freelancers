package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;

import java.util.Objects;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IssuePropose {

    private final ProposeRepository proposeRepository;
    private final ProjectRepository projectRepository;

    public IssuePropose(
            ProposeRepository proposeRepository,
            ProjectRepository projectRepository
    ) {
        this.proposeRepository = proposeRepository;
        this.projectRepository = projectRepository;
    }

    public void execute(CreateProposeCommand command) {
        Propose propose = command.propose();
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        if (command.user() == null || command.user().getFreelancerProfile() == null) {
            throw new BusinessException("Usuário não possui perfil de freelancer");
        }

        Freelancer freelancer = new Freelancer();
        freelancer.setId(command.user().getFreelancerProfile().getId());

        Project project = this.projectRepository.findByIdIfVisible(command.projectId(), command.user().getId())
            .orElseThrow(() -> new NotFoundException("Projeto não encontrado: " + command.projectId()));
        
        if (Objects.equals(command.user().getId(),project.getContractor().getUser().getId())) {
            throw new BusinessException("O contratante não pode enviar proposta para seu próprio projeto.");
        }

        Optional<Propose> existentPropose = this.proposeRepository.findByFreelancerIdAndProjectId(freelancer.getId(), project.getId());
        if (existentPropose.isPresent()) {
            Propose existentProposeValue = existentPropose.get();
            if (existentProposeValue.getStatus() == ProposeStatus.CANCELED || existentProposeValue.getStatus() == ProposeStatus.REJECTED) {
                this.proposeRepository.deleteById(existentProposeValue.getId());

                propose.setProject(project);
                propose.setFreelancer(freelancer);
                this.proposeRepository.create(propose);
                return;
            }
            throw new BusinessException("O freelancer já enviou uma proposta para este projeto");
        }

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new BusinessException("Não é possível enviar proposta para projeto com status: " + project.getStatus());
        }

        propose.setFreelancer(freelancer);
        propose.setProject(project);

        this.proposeRepository.create(propose);
  }
}