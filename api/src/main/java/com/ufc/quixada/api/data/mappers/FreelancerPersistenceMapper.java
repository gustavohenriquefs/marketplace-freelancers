package com.ufc.quixada.api.data.mappers;

import com.ufc.quixada.api.data.models.FreelancerJpaEntity;
import com.ufc.quixada.api.domain.entities.Freelancer;
import org.springframework.stereotype.Component;

@Component
public class FreelancerPersistenceMapper {

    public Freelancer toDomain(FreelancerJpaEntity entity) {
        return new Freelancer(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPrice()
        );
    }
}