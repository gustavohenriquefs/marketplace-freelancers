package com.ufc.quixada.api.integration;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.usecases.*;
import com.ufc.quixada.api.domain.entities.*;
import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import com.ufc.quixada.api.domain.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AnswerProposeIntegrationTest {

    @Autowired
    private CreateUser createUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CreateProject createProject;

    @Autowired
    private IssuePropose issuePropose;

    @Autowired
    private AnswerPropose answerPropose;

    @Autowired
    private ProposeRepository proposeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GetProjectById getProjectById;

    @Autowired
    private com.ufc.quixada.api.infrastructure.repositories.JpaProjectRepository jpaProjectRepository;

    @Test
    public void fullFlowAcceptProposeUpdatesProjectAndDoesNotStackOverflow() {
        // Create contractor user
        User contractorUser = new User();
        contractorUser.setName("Contractor");
        contractorUser.setEmail("contractor@example.com");
        contractorUser.setPassword("pass");
        createUser.execute(contractorUser);
        contractorUser = userRepository.findByEmail(contractorUser.getEmail()).orElseThrow();
        assertNotNull(contractorUser.getId());
        Long contractorProfileId = contractorUser.getContractorProfile().getId();

        // Create freelancer user
        User freelancerUser = new User();
        freelancerUser.setName("Freelancer");
        freelancerUser.setEmail("freelancer@example.com");
        freelancerUser.setPassword("pass");
        createUser.execute(freelancerUser);
        freelancerUser = userRepository.findByEmail(freelancerUser.getEmail()).orElseThrow();
        assertNotNull(freelancerUser.getId());
        Long freelancerProfileId = freelancerUser.getFreelancerProfile().getId();

        // Create supporting data: category, subcategory, skill
        Category cat = new Category(null, "Cat 1");
        cat = categoryRepository.save(cat);
        Subcategory sub = new Subcategory(null, "Sub 1", cat);
        sub = subcategoryRepository.save(sub);
        Skill skill = skillRepository.findAll().stream()
                .filter(s -> "Java".equals(s.getName()))
                .findFirst()
                .orElseGet(() -> skillRepository.save(new Skill(null, "Java")));


        // Create project
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Desc");
        project.setBudget(BigDecimal.TEN);
        project.setStatus(ProjectStatus.OPEN);
        project.setExperienceLevel(ExperienceLevel.BEGINNER);
        project.setDeadlineInDays(7L);
        project.setIsPublic(true);
        project.setCategory(cat);
        project.setSubcategory(sub);
        project.setSkills(List.of(skill));

        Project created = createProject.execute(project, contractorProfileId);
        assertNotNull(created.getId());

        // Create propose by freelancer
        Propose propose = new Propose();
        propose.setPrice(BigDecimal.valueOf(100));
        propose.setDuration(5);
        propose.setDescription("I can do it");

        CreateProposeCommand createCommand = new CreateProposeCommand(propose, freelancerUser, created.getId());
        issuePropose.execute(createCommand);

        // Verify propose exists
        var optional = proposeRepository.findByFreelancerIdAndProjectId(freelancerProfileId, created.getId());
        assertTrue(optional.isPresent());
        Propose savedPropose = optional.get();

        // Accept propose
        UpdateProposeStatusCommand accept = new UpdateProposeStatusCommand(savedPropose.getId(), ProposeStatus.ACCEPTED, contractorUser);
        answerPropose.execute(accept);

        // After accepting, propose status should be ACCEPTED
        var fetched = proposeRepository.findById(savedPropose.getId()).orElseThrow();
        assertEquals(ProposeStatus.ACCEPTED, fetched.getStatus());

        // Project should now include freelancer and be WAITING_PAYMENT
        Project updatedProject = projectRepository.findByIdIfVisible(created.getId(), contractorUser.getId()).orElseThrow();
        assertEquals(ProjectStatus.WAITING_PAYMENT, updatedProject.getStatus());

        // Inspect JPA model directly to ensure relationship exists in DB
        var jpa = jpaProjectRepository.findById(created.getId()).orElseThrow();
        assertNotNull(jpa.getFreelancers(), "JPA freelancers set should not be null");
        assertTrue(jpa.getFreelancers().stream().anyMatch(f -> f.getId().equals(freelancerProfileId)), "JPA freelancers should contain the accepted freelancer");

        assertNotNull(updatedProject.getFreelancers(), "Mapped domain freelancers should not be null");
        assertTrue(updatedProject.getFreelancers().stream().anyMatch(f -> f.getId().equals(freelancerProfileId)));

        // Mapping/DTO retrieval should not cause StackOverflow
        final Long savedProjectId = created.getId();
        final Long contractorUserId = contractorUser.getId();
        assertDoesNotThrow(() -> getProjectById.execute(savedProjectId, contractorUserId));
    }
}
