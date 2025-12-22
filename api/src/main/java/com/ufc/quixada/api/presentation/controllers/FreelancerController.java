package com.ufc.quixada.api.presentation.controllers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.usecases.GetFreelancers;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import com.ufc.quixada.api.presentation.mappers.FreelancerRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/freelancers")
public class FreelancerController {

    private final GetFreelancers getFreelancersUseCase;
    private final FreelancerRestMapper mapper;

    public FreelancerController(GetFreelancers useCase, FreelancerRestMapper mapper) {
        this.getFreelancersUseCase = useCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FreelancerResponseDTO>> getAll() {
        // 1. Executa Use Case (Retorna Domínio)
        List<Freelancer> domainList = getFreelancersUseCase.execute();

        // 2. Converte Domínio -> DTO (JSON)
        List<FreelancerResponseDTO> dtoList = domainList.stream()
                .map(mapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}