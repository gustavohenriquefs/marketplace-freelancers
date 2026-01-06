package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllContractorProjectsTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private GetAllContractorProjects getAllContractorProjects;

    @Test
    void shouldReturnEmptyWhenUserIdIsNull() {
        List<Project> result = getAllContractorProjects.execute(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(projectRepository);
    }

    @Test
    void shouldReturnEmptyWhenRepositoryReturnsEmpty() {
        when(projectRepository.findAllByContractorId(10L)).thenReturn(List.of());

        List<Project> result = getAllContractorProjects.execute(10L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository).findAllByContractorId(10L);
    }

    @Test
    void shouldReturnProjectsWhenRepositoryReturnsNonEmpty() {
        Project p = new Project();
        p.setId(1L);

        when(projectRepository.findAllByContractorId(10L)).thenReturn(List.of(p));

        List<Project> result = getAllContractorProjects.execute(10L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(projectRepository).findAllByContractorId(10L);
    }
}
