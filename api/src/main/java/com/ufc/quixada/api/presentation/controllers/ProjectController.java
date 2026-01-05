package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.command.CreateProposeCommand;
import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.application.mappers.UserMapper;
import com.ufc.quixada.api.application.usecases.*;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import com.ufc.quixada.api.presentation.dtos.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final CreateProject createProjectUseCase;
    private final GetAllProjects getAllProjectsUseCase;
    private final GetProjectById getProjectByIdUseCase;
    private final CreatePropose createPropose;

    private final AnswerPropose answerPropose;
    private final ProjectMapper projectMapper;
    private final ProposeMapper proposeMapper;

    public ProjectController(
            CreateProject createProject,
            GetAllProjects getAllProjects,
            GetProjectById getProjectById,
            AnswerPropose answerPropose,
            CreatePropose createPropose,
            ProjectMapper projectMapper,
            ProposeMapper proposeMapper
    ) {
        this.createProjectUseCase = createProject;
        this.getAllProjectsUseCase = getAllProjects;
        this.getProjectByIdUseCase = getProjectById;
        this.answerPropose = answerPropose;
        this.createPropose = createPropose;
        this.projectMapper = projectMapper;
        this.proposeMapper = proposeMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        log.info("=== Buscando todos os projetos ===");

        List<Project> projects = getAllProjectsUseCase.execute();
        List<ProjectResponseDTO> response = projects.stream()
                .map(projectMapper::toDto)
                .toList();

        log.info("Total de projetos encontrados: {}", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        log.info("=== Buscando projeto por ID: {} ===", id);

        Project project = getProjectByIdUseCase.execute(id);
        ProjectResponseDTO response = projectMapper.toDto(project);

        log.info("Projeto encontrado: {}", response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/proposes/{proposeId}/answer")
    public ResponseEntity<Void> answerPropose(
            @PathVariable Long proposeId,
            @Valid @RequestBody AnswerProposeRequestDTO request
    ) {
        log.info("=== Respondendo proposta ===");
        log.info("ProposeId: {}, NewStatus: {}", proposeId, request.newStatus());

        // Converte String → Enum
        ProposeStatus status = ProposeStatus.valueOf(request.newStatus());

        UpdateProposeStatusCommand command =
                new UpdateProposeStatusCommand(proposeId, status);

        this.answerPropose.execute(command);

        log.info("Proposta respondida com sucesso");

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/project/{projectId}/proposes")
    public ResponseEntity<Void> createPropose(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateProposeRequestDTO request
    ) {
        Propose propose = proposeMapper.toDomain(request);
        // Pega o usuário autenticado a partir do contexto de segurança
        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        log.info("=== Criando proposta ===");
        log.info("ProjectId: {}, Propose: {}", projectId, propose);

        CreateProposeCommand proposeCommand = new CreateProposeCommand(
                propose,
                user.getFreelancerProfile().getId(),
                projectId
        );

        this.createPropose.execute(proposeCommand);

        log.info("Proposta respondida com sucesso");

        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody CreateProjectJsonDTO projectReq) {
        log.info("=== Criando projeto via JSON ===");
        log.info("Project: {}", projectReq);

        Project project = this.projectMapper.toDomain(projectReq);

        // Pega o usuário autenticado a partir do contexto de segurança
        UserJpaModel user = (UserJpaModel) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();

        // Valida e obtém o ID do perfil de contratante
        if (user.getContractorProfile() == null) {
            throw new IllegalStateException("Usuário não tem um perfil de contratante para criar projetos.");
        }
        Long contractorId = user.getContractorProfile().getId();

        Project result = this.createProjectUseCase.execute(project, contractorId);

        if (result != null) {
            ProjectResponseDTO dto = projectMapper.toDto(result);
            log.info("Projeto criado com sucesso: ID={}", result.getId());
            return ResponseEntity.ok(dto);
        }
        log.warn("Falha ao criar projeto");
        return ResponseEntity.notFound().build();
    }
}
