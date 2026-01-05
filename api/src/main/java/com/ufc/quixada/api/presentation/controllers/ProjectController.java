package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.command.UpdateProposeStatusCommand;
import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.application.usecases.AnswerPropose;
import com.ufc.quixada.api.application.usecases.CreateProject;
import com.ufc.quixada.api.application.usecases.IssuePropose;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.presentation.dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final CreateProject createProjectUseCase;
    private final ProjectMapper projectMapper;

    private final IssuePropose issuePropose;
    private final ProposeMapper proposeMapper;
    private final AnswerPropose answerPropose;

    public ProjectController(CreateProject _createProject, IssuePropose issuePropose, ProposeMapper proposeMapper, AnswerPropose answerPropose) {
        this.createProjectUseCase = _createProject;
        this.issuePropose = issuePropose;
        this.answerPropose = answerPropose;

        this.proposeMapper = proposeMapper;
        this.projectMapper = ProjectMapper.INSTANCE;
    }

    @PatchMapping("/proposes/{proposeId}/answer")
    public ResponseEntity<Void> answer(
            @PathVariable Long proposeId,
            @Valid @RequestBody AnswerProposeRequestDTO request
    ) {
        UpdateProposeStatusCommand command =
                new UpdateProposeStatusCommand(proposeId, request.newStatus());

        this.answerPropose.execute(command);

        return ResponseEntity.noContent().build();

    }

    @PostMapping()
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @ModelAttribute ProjectRequestDTO projectReq) {
        Project project = this.projectMapper.toDomain(projectReq);
        Project result = this.createProjectUseCase.execute(project);

        if (result != null) {
            return ResponseEntity.ok(projectMapper.toDto(result));
        }

        return ResponseEntity.notFound().build();
    }
}
