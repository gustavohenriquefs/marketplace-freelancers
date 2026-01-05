package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
//import com.ufc.quixada.api.presentation.dtos.UserRequestDTO;
//import com.ufc.quixada.api.presentation.dtos.UserResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "freelancerProfile", ignore = true)
    @Mapping(target = "contractorProfile", ignore = true)
    User toDomain(UserJpaModel jpaEntity);
    //User toDomain(UserRequestDTO dto);
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "freelancerProfile", ignore = true)
    @Mapping(target = "contractorProfile", ignore = true)
    UserJpaModel toJpaEntity(User domain);

    @AfterMapping
    default void linkProfiles(User source, @MappingTarget UserJpaModel target) {
        if (target == null) return;

        if (source != null && source.getFreelancerProfile() != null) {
            FreelancerJpaModel freelancerProfile = new FreelancerJpaModel();
            freelancerProfile.setUser(target);
            target.setFreelancerProfile(freelancerProfile);
        }

        if (source != null && source.getContractorProfile() != null) {
            ContractorJpaModel contractorProfile = new ContractorJpaModel();
            contractorProfile.setUser(target);
            target.setContractorProfile(contractorProfile);
        }
    }

    //@Mapping(target = "id", source = "user.id")
    //@Mapping(target = "name", source = "user.name")
    //@Mapping(target = "email", source = "user.email")
    //UserResponseDTO toDTO(User domain);
}