package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Contractor;
import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ContractorMapper {
    ContractorMapper INSTANCE = Mappers.getMapper(ContractorMapper.class);

    Contractor toDomain(FreelancerResponseDTO freelancerResponseDTO);

    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "user", ignore = true)
    ContractorJpaModel toJpaEntity(Contractor contractor);

    @Mapping(target = "projects", ignore = true)
    Contractor toDomain(ContractorJpaModel contractorJpaEntity);
}
