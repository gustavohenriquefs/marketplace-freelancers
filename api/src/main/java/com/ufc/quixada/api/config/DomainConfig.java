package com.ufc.quixada.api.config;

import com.ufc.quixada.api.application.mappers.CategoryMapper;
import com.ufc.quixada.api.application.mappers.ContractorMapper;
import com.ufc.quixada.api.application.mappers.FileMapper;
import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.application.mappers.SkillMapper;
import com.ufc.quixada.api.application.mappers.SubcategoryMapper;
import com.ufc.quixada.api.application.mappers.UserMapper;
import com.ufc.quixada.api.application.usecases.*;
import com.ufc.quixada.api.domain.repositories.*;
import com.ufc.quixada.api.infrastructure.repositories.CategoryRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.ContractorRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.FileRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.FreelancerRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.JpaCategoryRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaContractorRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaFileRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaFreelancerRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaProjectRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaProposeRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaSkillRepository;
import com.ufc.quixada.api.infrastructure.repositories.JpaSubcategoryRepository;
import com.ufc.quixada.api.infrastructure.repositories.ProjectRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.ProposeRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.SkillRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.SubcategoryRepositoryImpl;
import com.ufc.quixada.api.infrastructure.repositories.UserJpaRepository;
import com.ufc.quixada.api.infrastructure.repositories.UserRepositoryImpl;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DomainConfig {

    @SuppressWarnings("unused")
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }

    @Bean
    public GetFreelancers getFreelancers(FreelancerRepository repo) {

        return new GetFreelancers(repo);
    }

    @Bean
    public IssuePropose issuePropose(
            ProposeRepository proposeRepository,
            ProjectRepository projectRepository
    ) {
        return new IssuePropose(proposeRepository, projectRepository);
    }

    @Bean
    public CreateProject createProject(
            FileRepository fileRepository,
            SubcategoryRepository subRepo,
            CategoryRepository categoryRepository,
            ProjectRepository projectRepository,
            SkillRepository skillRepository,
            ContractorRepository contractorRepository
    ) {

        return new CreateProject(
                fileRepository,
                subRepo,
                categoryRepository,
                projectRepository,
                skillRepository,
                contractorRepository
        );
    }

    @Bean
    public GetAllProjects getAllProjects(ProjectRepository projectRepository) {
        return new GetAllProjects(projectRepository);
    }

    @Bean
    public GetAllFreelancerProjects getAllFreelancerProjects(ProjectRepository projectRepository) {
        return new GetAllFreelancerProjects(projectRepository);
    }

    @Bean
    public GetAllContractorProjects getAllContractorProjects(ProjectRepository projectRepository) {
        return new GetAllContractorProjects(projectRepository);
    }

    @Bean
    public GetProjectById getProjectById(ProjectRepository projectRepository) {
        return new GetProjectById(projectRepository);
    }

    @Bean
    public AnswerPropose answerPropose(ProposeRepository proposeRepository, ProjectRepository projectRepository) {
        return new AnswerPropose(proposeRepository, projectRepository);
    }

    @Bean
    public CreateUser createUserUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return new CreateUser(userRepository, passwordEncoder);
    }

    @Bean
    public FreelancerRepository freelancerRepository(JpaFreelancerRepository jpaRepository, FreelancerMapper mapper){
        return new FreelancerRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public ProjectRepository projectRepository(JpaProjectRepository jpaRepository, ProjectMapper mapper, EntityManager entityManager){
        return new ProjectRepositoryImpl(
                jpaRepository,
                mapper,
                entityManager
        );
    }

    @Bean
    public ProposeRepository proposeRepository(JpaProposeRepository jpaRepository, ProposeMapper mapper, EntityManager entityManager){
        return new ProposeRepositoryImpl(
                jpaRepository,
                mapper,
                entityManager
        );
    }


    @Bean
    public UserRepository userRepository(
            UserJpaRepository userJpaRepository,
            UserMapper userMapper
    ){
        return new UserRepositoryImpl(
                userJpaRepository,
                userMapper
        );
    }

    @Bean
    public  ContractorRepository contractorRepository(
            JpaContractorRepository jpaRepository,
            ContractorMapper mapper
    ){
        return new ContractorRepositoryImpl(
                jpaRepository,
                mapper
        );
    }

    @Bean
    public CategoryRepository categoryRepository(
            JpaCategoryRepository jpaRepo,
            CategoryMapper mapper) {
        return new CategoryRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public SkillRepository skillRepository(
            JpaSkillRepository jpaRepo,
            SkillMapper mapper) {
        return new SkillRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public SubcategoryRepository subcategoryRepository(
            JpaSubcategoryRepository jpaRepo,
            SubcategoryMapper mapper) {
        return new SubcategoryRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public FileRepository fileRepository(
            JpaFileRepository jpaRepo,
            FileMapper mapper) {
        return new FileRepositoryImpl(jpaRepo, mapper);
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
}
