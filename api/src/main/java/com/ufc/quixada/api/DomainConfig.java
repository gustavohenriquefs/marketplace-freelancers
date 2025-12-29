package com.ufc.quixada.api;

import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.usecases.CreateProject;
import com.ufc.quixada.api.application.usecases.IssuePropose;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.application.usecases.GetFreelancers;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public GetFreelancers getFreelancers(FreelancerRepository repo) {

        return new GetFreelancers(repo);
    }

    @Bean
    public CreateProject createProject(JpaProjectRepository jpaRepository) {
        ProjectMapper mapper = ProjectMapper.INSTANCE;
        ProjectRepository repo = new ProjectRepositoryImpl(jpaRepository, mapper);
        return new CreateProject(repo);
    }

    @Bean
    public IssuePropose issuePropose(JpaProposeRepository jpaRepository) {
        var repo = new ProposeRepositoryImpl(jpaRepository);
        return new IssuePropose(repo);
    }

    @Bean
    public FreelancerRepository freelancerRepository(JpaFreelancerRepository jpaRepository){
        var mapper = FreelancerMapper.INSTANCE;
        return new FreelancerRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public ProjectRepository projectRepository(JpaProjectRepository jpaRepository){
        var mapper = ProjectMapper.INSTANCE;
        return new ProjectRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public ProposeRepository proposeRepository(JpaProposeRepository jpaRepository){
        return new ProposeRepositoryImpl(
                jpaRepository
        );
    }
}
