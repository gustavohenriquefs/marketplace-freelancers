package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectTest {

    @Mock private FileRepository fileRepository;
    @Mock private SubcategoryRepository subRepo;
    @Mock private CategoryRepository categoryRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private SkillRepository skillRepository;
    @Mock private ContractorRepository contractorRepository;

    @InjectMocks
    private CreateProject createProject;

    private static Project baseProject(Category category, Subcategory subcategory, List<Skill> skills) {
        Project p = new Project();
        p.setName("P");
        p.setDescription("D");
        p.setBudget(BigDecimal.TEN);
        p.setStatus(ProjectStatus.OPEN);
        p.setExperienceLevel(ExperienceLevel.BEGINNER);
        p.setDeadlineInDays(7L);
        p.setIsPublic(true);
        p.setCategory(category);
        p.setSubcategory(subcategory);
        p.setSkills(skills);
        return p;
    }

    @Test
    void shouldThrowWhenContractorNotFound() {
        Category cat = new Category(1L, "Cat");
        Subcategory sub = new Subcategory(2L, "Sub", cat);
        Project project = baseProject(cat, sub, List.of(new Skill(3L, "Java")));

        when(contractorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> createProject.execute(project, 10L));

        verify(projectRepository, never()).createProject(any());
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        Category cat = new Category(1L, "Cat");
        Subcategory sub = new Subcategory(2L, "Sub", cat);
        Project project = baseProject(cat, sub, List.of(new Skill(3L, "Java")));

        Contractor contractor = new Contractor();
        contractor.setId(10L);

        when(contractorRepository.findById(10L)).thenReturn(Optional.of(contractor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> createProject.execute(project, 10L));

        verify(projectRepository, never()).createProject(any());
    }

    @Test
    void shouldThrowWhenSubcategoryNotFound() {
        Category cat = new Category(1L, "Cat");
        Subcategory sub = new Subcategory(2L, "Sub", cat);
        Project project = baseProject(cat, sub, List.of(new Skill(3L, "Java")));

        Contractor contractor = new Contractor();
        contractor.setId(10L);

        when(contractorRepository.findById(10L)).thenReturn(Optional.of(contractor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(subRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> createProject.execute(project, 10L));

        verify(projectRepository, never()).createProject(any());
    }

    @Test
    void shouldThrowWhenSubcategoryDoesNotBelongToCategory() {
        Category cat1 = new Category(1L, "Cat1");
        Category cat2 = new Category(99L, "Cat2");
        Subcategory sub = new Subcategory(2L, "Sub", cat2);
        Project project = baseProject(cat1, sub, List.of(new Skill(3L, "Java")));

        Contractor contractor = new Contractor();
        contractor.setId(10L);

        when(contractorRepository.findById(10L)).thenReturn(Optional.of(contractor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat1));
        when(subRepo.findById(2L)).thenReturn(Optional.of(sub));

        assertThrows(IllegalArgumentException.class, () -> createProject.execute(project, 10L));

        verify(projectRepository, never()).createProject(any());
    }

    @Test
    void shouldThrowWhenNoValidSkills() {
        Category cat = new Category(1L, "Cat");
        Subcategory sub = new Subcategory(2L, "Sub", cat);
        Project project = baseProject(cat, sub, List.of(new Skill(3L, "Java")));

        Contractor contractor = new Contractor();
        contractor.setId(10L);

        when(contractorRepository.findById(10L)).thenReturn(Optional.of(contractor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(subRepo.findById(2L)).thenReturn(Optional.of(sub));
        when(skillRepository.findAllById(List.of(3L))).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> createProject.execute(project, 10L));

        verify(projectRepository, never()).createProject(any());
    }

    @Test
    void shouldCreateProjectOnSuccess() {
        Category cat = new Category(1L, "Cat");
        Subcategory sub = new Subcategory(2L, "Sub", cat);
        Skill skill = new Skill(3L, "Java");
        Project project = baseProject(cat, sub, List.of(skill));

        Contractor contractor = new Contractor();
        contractor.setId(10L);

        Project created = new Project();
        created.setId(123L);

        when(contractorRepository.findById(10L)).thenReturn(Optional.of(contractor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(subRepo.findById(2L)).thenReturn(Optional.of(sub));
        when(skillRepository.findAllById(List.of(3L))).thenReturn(List.of(skill));
        when(projectRepository.createProject(any(Project.class))).thenReturn(created);

        Project result = createProject.execute(project, 10L);

        assertEquals(123L, result.getId());

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).createProject(captor.capture());
        Project sent = captor.getValue();
        assertSame(contractor, sent.getContractor());
        assertSame(cat, sent.getCategory());
        assertSame(sub, sent.getSubcategory());
    }
}
