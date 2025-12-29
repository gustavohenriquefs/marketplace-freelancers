package com.ufc.quixada.api;

import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.application.usecases.CreateProject;
import com.ufc.quixada.api.application.usecases.IssuePropose;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.application.usecases.GetFreelancers;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.repositories.FreelancerRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.JpaFreelancerInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public GetFreelancers getFreelancers(FreelancerRepository repo) {
        return new GetFreelancers(repo);
    }

    @Bean
    public CreateProject createProject(ProjectRepository repo){
        return new CreateProject(repo);
    }

    @Bean
    public IssuePropose issuePropose(ProposeRepository repo) {
        return new IssuePropose(repo);
    }

    @Bean
    public FreelancerRepository freelancerRepository(JpaFreelancerInterface jpaRepository){
        var mapper = FreelancerMapper.INSTANCE;
        return new FreelancerRepositoryImpl(
                jpaRepository,
                mapper
        );
    }
}
