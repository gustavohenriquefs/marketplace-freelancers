package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.exceptions.BusinessException;
import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueProposeTest {

    @Mock
    private ProposeRepository proposeRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private IssuePropose issuePropose;

    private static User freelancerUser(Long userId, Long freelancerId) {
        User u = new User();
        u.setId(userId);
        Freelancer f = new Freelancer();
        f.setId(freelancerId);
        u.setFreelancerProfile(f);
        return u;
    }

    private static Project projectWithContractorUser(Long projectId, Long contractorUserId, ProjectStatus status) {
        User contractorUser = new User();
        contractorUser.setId(contractorUserId);

        Contractor contractor = new Contractor();
        contractor.setId(999L);
        contractor.setUser(contractorUser);

        Project p = new Project();
        p.setId(projectId);
        p.setContractor(contractor);
        p.setStatus(status);
        return p;
    }

    @Test
    void shouldThrowWhenUserHasNoFreelancerProfile() {
        User u = new User();
        u.setId(1L);

        Propose propose = new Propose();
        CreateProposeCommand cmd = new CreateProposeCommand(propose, u, 10L);

        assertThrows(BusinessException.class, () -> issuePropose.execute(cmd));

        verifyNoInteractions(projectRepository);
        verifyNoInteractions(proposeRepository);
    }

    @Test
    void shouldThrowWhenProjectNotFoundOrNotVisible() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> issuePropose.execute(new CreateProposeCommand(propose, u, 10L)));

        verify(proposeRepository, never()).create(any());
    }

    @Test
    void shouldThrowWhenContractorTriesToProposeOnOwnProject() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();
        Project project = projectWithContractorUser(10L, 1L, ProjectStatus.OPEN);

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(project));

        assertThrows(BusinessException.class, () -> issuePropose.execute(new CreateProposeCommand(propose, u, 10L)));

        verify(proposeRepository, never()).create(any());
    }

    @Test
    void shouldReplaceCanceledExistingPropose() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();
        Project project = projectWithContractorUser(10L, 2L, ProjectStatus.OPEN);

        Propose existing = new Propose();
        existing.setId(55L);
        existing.setStatus(ProposeStatus.CANCELED);

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(project));
        when(proposeRepository.findByFreelancerIdAndProjectId(100L, 10L)).thenReturn(Optional.of(existing));

        issuePropose.execute(new CreateProposeCommand(propose, u, 10L));

        verify(proposeRepository).deleteById(55L);
        ArgumentCaptor<Propose> captor = ArgumentCaptor.forClass(Propose.class);
        verify(proposeRepository).create(captor.capture());
        Propose created = captor.getValue();

        assertEquals(ProposeStatus.WAITING_ANALYSIS, created.getStatus());
        assertNotNull(created.getFreelancer());
        assertEquals(100L, created.getFreelancer().getId());
        assertNotNull(created.getProject());
        assertEquals(10L, created.getProject().getId());
    }

    @Test
    void shouldThrowWhenExistingProposeNotFinalCanceledOrRejected() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();
        Project project = projectWithContractorUser(10L, 2L, ProjectStatus.OPEN);

        Propose existing = new Propose();
        existing.setId(55L);
        existing.setStatus(ProposeStatus.WAITING_ANALYSIS);

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(project));
        when(proposeRepository.findByFreelancerIdAndProjectId(100L, 10L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class, () -> issuePropose.execute(new CreateProposeCommand(propose, u, 10L)));

        verify(proposeRepository, never()).create(any());
        verify(proposeRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldThrowWhenProjectNotOpen() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();
        Project project = projectWithContractorUser(10L, 2L, ProjectStatus.WAITING_PAYMENT);

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(project));
        when(proposeRepository.findByFreelancerIdAndProjectId(100L, 10L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> issuePropose.execute(new CreateProposeCommand(propose, u, 10L)));

        verify(proposeRepository, never()).create(any());
    }

    @Test
    void shouldCreateProposeOnSuccess() {
        User u = freelancerUser(1L, 100L);
        Propose propose = new Propose();
        Project project = projectWithContractorUser(10L, 2L, ProjectStatus.OPEN);

        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(project));
        when(proposeRepository.findByFreelancerIdAndProjectId(100L, 10L)).thenReturn(Optional.empty());

        issuePropose.execute(new CreateProposeCommand(propose, u, 10L));

        ArgumentCaptor<Propose> captor = ArgumentCaptor.forClass(Propose.class);
        verify(proposeRepository).create(captor.capture());
        Propose created = captor.getValue();

        assertEquals(ProposeStatus.WAITING_ANALYSIS, created.getStatus());
        assertNotNull(created.getFreelancer());
        assertEquals(100L, created.getFreelancer().getId());
        assertNotNull(created.getProject());
        assertEquals(10L, created.getProject().getId());
    }
}
