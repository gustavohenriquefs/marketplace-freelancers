package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.mappers.ProjectMapper;
import com.ufc.quixada.api.application.usecases.CreateProject;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.presentation.dtos.ProjectRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProjectResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/contractors")
public class ContractorController {
    private final CreateProject createProjectUseCase;
    private final ProjectMapper projectMapper;

    public ContractorController(CreateProject _createProject) {
        this.createProjectUseCase = _createProject;
        this.projectMapper = ProjectMapper.INSTANCE;
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @ModelAttribute ProjectRequestDTO projectReq) {
        Project project =  this.projectMapper.toDomain(projectReq);
        Project result = this.createProjectUseCase.execute(project);

        if (result != null) {
            return ResponseEntity.ok(projectMapper.toDto(result));
        }

        return ResponseEntity.notFound().build();
    }
}
