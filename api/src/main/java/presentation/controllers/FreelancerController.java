package presentation.controllers;

import domain.entities.Freelancer;
import domain.usecases.GetFreelancers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import presentation.dtos.FreelancerResponseDTO;
import presentation.mappers.FreelancerRestMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/freelancers")
public class FreelancerController {
    private final GetFreelancers _getFreelancersUseCase;
    private final FreelancerRestMapper mapper;

    public  FreelancerController(GetFreelancers getFreelancers, FreelancerRestMapper mapper) {
        this._getFreelancersUseCase = getFreelancers;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FreelancerResponseDTO>> get() {
        List<Freelancer> domainEntities = this._getFreelancersUseCase.getFreelancers();

        List<FreelancerResponseDTO> responseDTO = domainEntities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responseDTO);
    }
}
