package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.application.usecases.IssuePropose;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.application.usecases.GetFreelancers;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import com.ufc.quixada.api.presentation.dtos.ProposeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freelancers")
public class FreelancerController {

    private final GetFreelancers getFreelancersUseCase;
    private final IssuePropose issuePropose;
    private final ProposeMapper proposeMapper;
    private final FreelancerMapper freelancerMapper;

    public FreelancerController(GetFreelancers useCase, IssuePropose issuePropose, FreelancerMapper freelancerMapper) {
        this.getFreelancersUseCase = useCase;
        this.issuePropose = issuePropose;
        this.freelancerMapper = freelancerMapper;
        this.proposeMapper = ProposeMapper.INSTANCE;
    }

    @PostMapping("/projects/{idProject}/proposes")
    public ResponseEntity<ProposeResponseDTO> issuePropose(
            @PathVariable String idProject,
            @PathVariable String idPropose,
            @RequestBody CreateProposeRequestDTO proposeRequestDTO
    ) {
        // 1. Converte DTO (JSON) -> Domínio
        Propose proposeDomain = proposeMapper.toDomain(proposeRequestDTO);
        // 2. Executa Use Case (Retorna Domínio)
        Propose createdProposeDomain = issuePropose.execute(proposeDomain);
        // 3. Converte Domínio -> DTO (JSON)
        ProposeResponseDTO proposeResponseDTO = proposeMapper.toDTO(createdProposeDomain);

        return ResponseEntity.status(201).body(proposeResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FreelancerResponseDTO>> getAll() {
        // 1. Executa Use Case (Retorna Domínio)
        List<Freelancer> domainList = getFreelancersUseCase.execute();

        // 2. Converte Domínio -> DTO (JSON)
        List<FreelancerResponseDTO> dtoList = domainList.stream()
                .map(freelancerMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}