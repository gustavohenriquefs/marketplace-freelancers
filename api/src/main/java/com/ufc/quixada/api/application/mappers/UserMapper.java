package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {FreelancerMapper.class, ContractorMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "freelancerProfile", source = "freelancerProfile", qualifiedByName = "mapFreelancerShallow")
    @Mapping(target = "contractorProfile", source = "contractorProfile", qualifiedByName = "mapContractorShallow")
    User toDomain(UserJpaModel jpaEntity);

    @Named("mapFreelancerShallow")
    default com.ufc.quixada.api.domain.entities.Freelancer mapFreelancerShallow(com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel jpa) {
        if (jpa == null) return null;
        return new com.ufc.quixada.api.domain.entities.Freelancer(jpa.getId(), null, null, null);
    }

    @Named("mapContractorShallow")
    default com.ufc.quixada.api.domain.entities.Contractor mapContractorShallow(com.ufc.quixada.api.infrastructure.models.ContractorJpaModel jpa) {
        if (jpa == null) return null;
        return new com.ufc.quixada.api.domain.entities.Contractor(jpa.getId(), null, null);
    }

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

}