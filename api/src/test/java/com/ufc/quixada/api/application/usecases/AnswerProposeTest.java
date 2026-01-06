package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerProposeTest {

    @Mock
    private ProposeRepository proposeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private AnswerPropose answerPropose;

    @Test
    public void acceptedProposeShouldBeSavedAndProjectUpdated() {
        // Arrange
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var freelancer = new Freelancer();
        freelancer.setId(200L);
        var freelancerUser = new User();
        freelancerUser.setId(2L);
        freelancerUser.setFreelancerProfile(freelancer);
        freelancer.setUser(freelancerUser);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setStatus(ProjectStatus.OPEN);
        project.setFreelancers(new java.util.ArrayList<>());

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        // Act
        answerPropose.execute(command);

        // Assert
        verify(proposeRepository, times(1)).save(propose);
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository, times(1)).updateProject(projectCaptor.capture());

        Project updatedProject = projectCaptor.getValue();
        assertEquals(ProjectStatus.WAITING_PAYMENT, updatedProject.getStatus());
        assertTrue(updatedProject.getFreelancers().stream().anyMatch(f -> f.getId().equals(200L)));
    }

    @Test
    public void shouldThrowWhenProposeNotFound() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        when(proposeRepository.findById(20L)).thenReturn(Optional.empty());

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        assertThrows(com.ufc.quixada.api.application.exceptions.NotFoundException.class, () -> answerPropose.execute(command));

        verify(proposeRepository, never()).save(any());
        verify(projectRepository, never()).updateProject(any());
    }

    @Test
    public void shouldThrowAccessDeniedWhenContractorDoesNotOwnProject() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var otherContractor = new Contractor();
        otherContractor.setId(999L);

        var project = new Project();
        project.setId(10L);
        project.setContractor(otherContractor);
        project.setFreelancers(new java.util.ArrayList<>());

        var freelancer = new Freelancer();
        freelancer.setId(200L);
        var freelancerUser = new User();
        freelancerUser.setId(2L);
        freelancer.setUser(freelancerUser);

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        assertThrows(AccessDeniedException.class, () -> answerPropose.execute(command));

        verify(proposeRepository, never()).save(any());
        verify(projectRepository, never()).updateProject(any());
    }

    @Test
    public void shouldThrowWhenContractorAnswersOwnPropose() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setFreelancers(new java.util.ArrayList<>());

        var freelancer = new Freelancer();
        freelancer.setId(200L);
        freelancer.setUser(contractorUser);

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        assertThrows(BusinessException.class, () -> answerPropose.execute(command));

        verify(proposeRepository, never()).save(any());
        verify(projectRepository, never()).updateProject(any());
    }

    @Test
    public void shouldThrowWhenProjectIsOnGoing() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var freelancer = new Freelancer();
        freelancer.setId(200L);
        var freelancerUser = new User();
        freelancerUser.setId(2L);
        freelancer.setUser(freelancerUser);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setStatus(ProjectStatus.IN_PROGRESS);
        project.setFreelancers(new java.util.ArrayList<>());

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        assertThrows(BusinessException.class, () -> answerPropose.execute(command));

        verify(proposeRepository, never()).save(any());
        verify(projectRepository, never()).updateProject(any());
    }

    @Test
    public void acceptedProposeShouldInitializeFreelancersListWhenNull() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var freelancer = new Freelancer();
        freelancer.setId(200L);
        var freelancerUser = new User();
        freelancerUser.setId(2L);
        freelancer.setUser(freelancerUser);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setStatus(ProjectStatus.OPEN);
        project.setFreelancers(null);

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        answerPropose.execute(command);

        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).updateProject(projectCaptor.capture());
        assertNotNull(projectCaptor.getValue().getFreelancers());
        assertTrue(projectCaptor.getValue().getFreelancers().stream().anyMatch(f -> f.getId().equals(200L)));
    }

    @Test
    public void shouldThrowWhenAcceptingProposeWithInvalidFreelancer() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var freelancer = new Freelancer();
        freelancer.setId(null);
        var freelancerUser = new User();
        freelancerUser.setId(2L);
        freelancer.setUser(freelancerUser);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setStatus(ProjectStatus.OPEN);
        project.setFreelancers(new java.util.ArrayList<>());

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.WAITING_ANALYSIS);
        propose.setProject(project);
        propose.setFreelancer(freelancer);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.ACCEPTED, contractorUser);

        assertThrows(BusinessException.class, () -> answerPropose.execute(command));

        verify(projectRepository, never()).updateProject(any());
    }

    @Test
    public void cannotAnswerFinalizedPropose() {
        var contractor = new Contractor();
        contractor.setId(100L);
        var contractorUser = new User();
        contractorUser.setId(1L);
        contractorUser.setContractorProfile(contractor);

        var project = new Project();
        project.setId(10L);
        project.setContractor(contractor);
        project.setFreelancers(new java.util.ArrayList<>());

        var propose = new Propose();
        propose.setId(20L);
        propose.setStatus(ProposeStatus.ACCEPTED);
        propose.setProject(project);

        when(proposeRepository.findById(20L)).thenReturn(Optional.of(propose));

        var command = new UpdateProposeStatusCommand(20L, ProposeStatus.CANCELED, contractorUser);

        assertThrows(BusinessException.class, () -> answerPropose.execute(command));

        verify(proposeRepository, never()).save(any());
        verify(projectRepository, never()).updateProject(any());
    }
}
