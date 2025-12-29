package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaEntity;
import com.ufc.quixada.api.presentation.dtos.FreelancerRequestDTO;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//@Mapper(uses = {MetaRendimentoMapper.class, ItemRendimentoMapper.class})
@Mapper(componentModel = "spring", uses = {})
public interface FreelancerMapper {

    FreelancerMapper INSTANCE = Mappers.getMapper(FreelancerMapper.class);

    Freelancer toDomain(FreelancerJpaEntity jpaEntity);
    Freelancer toDomain(FreelancerRequestDTO dto);
    FreelancerJpaEntity toJpaEntity(Freelancer domain);
    FreelancerResponseDTO toDTO(Freelancer domain);
}
