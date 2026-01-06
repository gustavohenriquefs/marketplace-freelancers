package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.application.exceptions.NotFoundException;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProjectByIdTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private GetProjectById getProjectById;

    @Test
    void shouldThrowNotFoundWhenUserCannotViewProject() {
        when(projectRepository.userCanViewProject(1L, 10L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> getProjectById.execute(10L, 1L));

        verify(projectRepository, never()).findByIdIfVisible(anyLong(), anyLong());
    }

    @Test
    void shouldThrowNotFoundWhenProjectNotVisibleInRepo() {
        when(projectRepository.userCanViewProject(1L, 10L)).thenReturn(true);
        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> getProjectById.execute(10L, 1L));
    }

    @Test
    void shouldReturnProjectWhenVisible() {
        Project p = new Project();
        p.setId(10L);

        when(projectRepository.userCanViewProject(1L, 10L)).thenReturn(true);
        when(projectRepository.findByIdIfVisible(10L, 1L)).thenReturn(Optional.of(p));

        Project result = getProjectById.execute(10L, 1L);

        assertEquals(10L, result.getId());
    }
}
