package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class ProjectMapperCycleTest {

    @Autowired
    private ProjectMapper projectMapper;

    @Test
    public void mappingShouldNotCauseStackOverflow() {
        UserJpaModel user = new UserJpaModel();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        FreelancerJpaModel freelancer = new FreelancerJpaModel();
        freelancer.setId(2L);
        freelancer.setUser(user);

        user.setFreelancerProfile(freelancer);

        ProjectJpaModel project = new ProjectJpaModel();
        project.setId(3L);
        project.setFreelancers(new HashSet<>());
        project.getFreelancers().add(freelancer);

        assertDoesNotThrow(() -> projectMapper.toDomain(project));
    }
}
