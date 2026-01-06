package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.presentation.dtos.FreelancerRequestDTO;
import com.ufc.quixada.api.presentation.dtos.FreelancerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FreelancerMapper {

    FreelancerMapper INSTANCE = Mappers.getMapper(FreelancerMapper.class);

    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "proposes", ignore = true)
    @Mapping(target = "user.freelancerProfile", ignore = true)
    @Mapping(target = "user.contractorProfile", ignore = true)
    Freelancer toDomain(FreelancerJpaModel jpaEntity);
    Freelancer toDomain(FreelancerRequestDTO dto);
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "proposes", ignore = true)
    @Mapping(target = "user", ignore = true)
    FreelancerJpaModel toJpaEntity(Freelancer domain);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    FreelancerResponseDTO toDTO(Freelancer domain);
}
