package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;
import com.ufc.quixada.api.presentation.dtos.AnswerProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProposeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProposeMapper {
    ProposeMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ProposeMapper.class);

    ProposeJpaEntity toJpaEntity(Propose propose);

    @Mapping(target = "freelancer.user.freelancerProfile", ignore = true)
    @Mapping(target = "freelancer.user.contractorProfile", ignore = true)
    @Mapping(target = "freelancer.projects", ignore = true)
    @Mapping(target = "freelancer.proposes", ignore = true)
    @Mapping(target = "project.proposes", ignore = true)
    @Mapping(target = "project.files", ignore = true)
    @Mapping(target = "project.contractor.user.freelancerProfile", ignore = true)
    @Mapping(target = "project.contractor.user.contractorProfile", ignore = true)
    @Mapping(target = "project.contractor.projects", ignore = true)
    Propose toDomain(ProposeJpaEntity proposeJpaEntity);

    Propose toDomain(CreateProposeRequestDTO proposeResponseDTO);
    Propose toDomain(AnswerProposeRequestDTO answererProposeRequestDTO);
    ProposeResponseDTO toDTO(Propose propose);
}
