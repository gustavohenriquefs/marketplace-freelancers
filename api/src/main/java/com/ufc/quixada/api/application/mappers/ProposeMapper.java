package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;
import com.ufc.quixada.api.presentation.dtos.AnswerProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProposeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProposeMapper {
    ProposeMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ProposeMapper.class);

    @Mapping(target = "freelancer", source = "freelancer", qualifiedByName = "freelancerRef")
    @Mapping(target = "project", source = "project", qualifiedByName = "projectRef")
    ProposeJpaEntity toJpaEntity(Propose propose);

    @Named("freelancerRef")
    default FreelancerJpaModel freelancerRef(Freelancer freelancer) {
        if (freelancer == null) {
            return null;
        }
        FreelancerJpaModel ref = new FreelancerJpaModel();
        ref.setId(freelancer.getId());
        return ref;
    }

    @Named("projectRef")
    default ProjectJpaModel projectRef(Project project) {
        if (project == null) {
            return null;
        }
        ProjectJpaModel ref = new ProjectJpaModel();
        ref.setId(project.getId());
        return ref;
    }

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
