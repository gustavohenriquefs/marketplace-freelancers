package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Contractor;
import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContractorMapper {
    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    Contractor toDomain(FreelancerResponseDTO freelancerResponseDTO);

    ContractorJpaModel toJpaEntity(Contractor contractor);

    Contractor toDomain(ContractorJpaModel contractorJpaEntity);
}
