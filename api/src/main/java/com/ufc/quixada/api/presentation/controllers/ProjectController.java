package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.application.mappers.UserMapper;
import com.ufc.quixada.api.application.usecases.*;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import com.ufc.quixada.api.presentation.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final CreateProject createProject;
    private final GetAllProjects getAllProjects;
    private final GetAllContractorProjects getAllContractorProjects;
    private final GetAllFreelancerProjects getAllFreelancerProjects;
    private final GetProjectById getProjectById;
    private final IssuePropose createPropose;

    private final AnswerPropose answerPropose;
    private final ProjectMapper projectMapper;
    private final ProposeMapper proposeMapper;
    private final UserMapper userMapper;

    public ProjectController(
            CreateProject createProject,
            GetAllProjects getAllProjects,
            GetAllContractorProjects getAllContractorProjects,
            GetAllFreelancerProjects getAllFreelancerProjects,
            GetProjectById getProjectById,
            AnswerPropose answerPropose,
            IssuePropose createPropose,
            ProjectMapper projectMapper,
            ProposeMapper proposeMapper,
            UserMapper userMapper
    ) {
        this.createProject = createProject;
        this.getAllProjects = getAllProjects;
        this.getAllContractorProjects = getAllContractorProjects;
        this.getAllFreelancerProjects = getAllFreelancerProjects;
        this.getProjectById = getProjectById;
        this.answerPropose = answerPropose;
        this.createPropose = createPropose;
        this.projectMapper = projectMapper;
        this.proposeMapper = proposeMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {

        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        User userDomain = userMapper.toDomain(user);

        List<Project> projects = getAllProjects.execute(userDomain.getId());
        List<ProjectResponseDTO> response = projects.stream()
                .map(projectMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/freelancer/findAll")
    public ResponseEntity<List<ProjectResponseDTO>> getAllFreelancerProjects() {

        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        if (user.getFreelancerProfile() == null) {
            throw new IllegalStateException("Usuário não tem perfil de freelancer para buscar projetos.");
        }

        List<Project> projects = getAllFreelancerProjects.execute(user.getFreelancerProfile().getId());
        List<ProjectResponseDTO> response = projects.stream()
                .map(projectMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/contractor/findAll")
    public ResponseEntity<List<ProjectResponseDTO>> getAllContractorProjects() {

        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        List<Project> projects = getAllContractorProjects.execute(user.getContractorProfile().getId());
        List<ProjectResponseDTO> response = projects.stream()
                .map(projectMapper::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {

        Long userId = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserJpaModel u) {
            userId = u.getId();
        }

        Project project = getProjectById.execute(id, userId);
        ProjectResponseDTO response = projectMapper.toDto(project);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proposes/{proposeId}/answer")
    public ResponseEntity<Void> answerPropose(
            @PathVariable Long proposeId,
            @Valid @RequestBody AnswerProposeRequestDTO request
    ) {

        ProposeStatus status = ProposeStatus.valueOf(request.newStatus());

        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        if (user.getContractorProfile() == null) {
            throw new IllegalStateException("Usuário não tem perfil de contratante para responder propostas.");
        }

        User userDomain = userMapper.toDomain(user);

        UpdateProposeStatusCommand command =
            new UpdateProposeStatusCommand(proposeId, status, userDomain);

        this.answerPropose.execute(command);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/project/{projectId}/proposes")
    public ResponseEntity<Void> createPropose(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateProposeRequestDTO request
    ) {
        Propose propose = proposeMapper.toDomain(request);
        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        
        User userDomain = userMapper.toDomain(user);

        CreateProposeCommand proposeCommand = new CreateProposeCommand(
                propose,
                userDomain,
                projectId
        );

        this.createPropose.execute(proposeCommand);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectJsonDTO projectReq) {

        Project project = this.projectMapper.toDomain(projectReq);

        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        if (user.getContractorProfile() == null) {
            throw new IllegalStateException("Usuário não tem um perfil de contratante para criar projetos.");
        }
        Long contractorId = user.getContractorProfile().getId();

        Project result = this.createProject.execute(project, contractorId);

        if (result != null) {
            ProjectResponseDTO dto = projectMapper.toDto(result);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
}
