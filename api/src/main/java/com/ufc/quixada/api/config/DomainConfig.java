package com.ufc.quixada.api.config;

import com.ufc.quixada.api.application.mappers.*;
import com.ufc.quixada.api.application.usecases.*;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.domain.repositories.ProjectRepository;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.repositories.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DomainConfig {

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }

    @Bean
    public GetFreelancers getFreelancers(FreelancerRepository repo) {

        return new GetFreelancers(repo);
    }

    @Bean
    public CreateProject createProject(JpaProjectRepository jpaRepository, ProjectMapper mapper) {
        ProjectRepository repo = new ProjectRepositoryImpl(jpaRepository, mapper);
        return new CreateProject(repo);
    }

    @Bean
    public IssuePropose issuePropose(JpaProposeRepository jpaRepository, ProposeMapper mapper) {
        var repo = new ProposeRepositoryImpl(jpaRepository, mapper);
        return new IssuePropose(repo);
    }

    @Bean
    public AnswerPropose answerPropose(JpaProposeRepository jpaRepository, ProposeMapper mapper) {
        var repo = new ProposeRepositoryImpl(jpaRepository, mapper);
        return new AnswerPropose(repo);
    }

    @Bean
    public FreelancerRepository freelancerRepository(JpaFreelancerRepository jpaRepository, FreelancerMapper mapper){
        return new FreelancerRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public ProjectRepository projectRepository(JpaProjectRepository jpaRepository, ProjectMapper mapper){
        return new ProjectRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public ProposeRepository proposeRepository(JpaProposeRepository jpaRepository, ProposeMapper mapper){
        return new ProposeRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public ExceptionHandlerAuthenticationEntryPoint authenticationEntryPoint() {
        return new ExceptionHandlerAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserJpaRepository userJpaRepository,
            JpaFreelancerRepository freelancerJpaRepository,
            JpaContractorRepository contractorJpaRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            ContractorMapper contractorMapper,
            FreelancerMapper freelancerMapper) {

        var userRepo = new UserRepositoryImpl(userJpaRepository, userMapper);
        var contractorRepo = new ContractorRepositoryImpl(contractorJpaRepository, contractorMapper);
        var freelancerRepo = new FreelancerRepositoryImpl(freelancerJpaRepository, freelancerMapper);
        return new CreateUserUseCase(userRepo, freelancerRepo, contractorRepo, passwordEncoder);
    }

    @Bean
    public UserRepositoryImpl userRepositoryImpl(
            UserJpaRepository userJpaRepository,
            UserMapper userMapper
    ){
        return new UserRepositoryImpl(
                userJpaRepository,
                userMapper
        );
    }
}
