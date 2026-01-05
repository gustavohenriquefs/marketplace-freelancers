package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;
import com.ufc.quixada.api.presentation.dtos.AnswerProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProposeResponseDTO;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProposeMapper {
    ProposeMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ProposeMapper.class);

    ProposeJpaEntity toJpaEntity(Propose propose);
    Propose toDomain(ProposeJpaEntity proposeJpaEntity);
    Propose toDomain(CreateProposeRequestDTO proposeResponseDTO);
    Propose toDomain(AnswerProposeRequestDTO answererProposeRequestDTO);
    ProposeResponseDTO toDTO(Propose propose);
}
