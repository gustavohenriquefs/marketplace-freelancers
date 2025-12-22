package com.ufc.quixada.api.presentation.mappers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class FreelancerRestMapper {
    public FreelancerResponseDTO toDTO(Freelancer domain) {
        return new FreelancerResponseDTO(
                domain.getId().toString(),
                domain.getName(),
                domain.getEmail()
        );
    }
}