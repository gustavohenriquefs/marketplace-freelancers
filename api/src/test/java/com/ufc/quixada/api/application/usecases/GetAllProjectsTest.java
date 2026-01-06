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
class GetAllProjectsTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private GetAllProjects getAllProjects;

    @Test
    void shouldReturnVisibleProjectsFromRepository() {
        Project p1 = new Project();
        p1.setId(1L);
        Project p2 = new Project();
        p2.setId(2L);

        when(projectRepository.findAllVisible(10L)).thenReturn(List.of(p1, p2));

        List<Project> result = getAllProjects.execute(10L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(projectRepository).findAllVisible(10L);
    }
}
