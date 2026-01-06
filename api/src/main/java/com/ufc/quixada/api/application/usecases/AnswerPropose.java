package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
public class AnswerPropose {

    private final ProposeRepository repository;
    private final ProjectRepository projectRepository;

    public AnswerPropose(ProposeRepository repository, ProjectRepository projectRepository) {
        this.repository = repository;
        this.projectRepository = projectRepository;
    }
    
    public void execute(UpdateProposeStatusCommand command) {
        Propose propose = repository.findById(command.proposeId())
                .orElseThrow(() -> new NotFoundException("Proposta não encontrada"));

        if (propose.isFinalized()) {
            throw new BusinessException("Proposta já finalizada");
        }

        if (!Objects.equals(propose.getProject().getContractor().getId(), command.user().getContractorProfile().getId())) {
            throw new AccessDeniedException("Usuário não autorizado a acessar esta informação");
        }

        if (Objects.equals(propose.getFreelancer().getUser(), command.user())) {
            throw new BusinessException("O contratante não pode responder à própria proposta");
        }

        if (propose.getProject().isOnGoing()) {
            throw new BusinessException("Não é possível atualizar proposta para projetos em andamento");
        }

        propose.updateStatus(command.newStatus());

        repository.save(propose);

        if(command.newStatus() == com.ufc.quixada.api.domain.enums.ProposeStatus.ACCEPTED) {
            Project project = propose.getProject();
            if (project.getFreelancers() == null) {
                project.setFreelancers(new java.util.ArrayList<>());
            }

            if (propose.getFreelancer() == null || propose.getFreelancer().getId() == null) {
                throw new com.ufc.quixada.api.application.exceptions.BusinessException("Freelancer inválido para aceitar proposta");
            }
            project.getFreelancers().add(propose.getFreelancer());
            project.setStatus(ProjectStatus.WAITING_PAYMENT);
            projectRepository.updateProject(project);
        }
    }
}
