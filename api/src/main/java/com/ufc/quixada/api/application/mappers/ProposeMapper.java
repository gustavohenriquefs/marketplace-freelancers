package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaModel;
import com.ufc.quixada.api.presentation.dtos.AnswerProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.CreateProposeRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProposeResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProposeMapper {
    ProposeMapper INSTANCE = org.mapstruct.factory.Mappers.getMapper(ProposeMapper.class);

    @Mapping(target = "freelancer", source = "freelancer", qualifiedByName = "freelancerRef")
    @Mapping(target = "project", source = "project", qualifiedByName = "projectRef")
    ProposeJpaModel toJpaEntity(Propose propose);

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

    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "project", ignore = true)
    Propose toDomain(ProposeJpaModel proposeJpaEntity);

    @AfterMapping
    default void setShallowRefs(ProposeJpaModel source, @MappingTarget Propose target) {
        if (source == null || target == null) return;
        if (source.getFreelancer() != null) {
            target.setFreelancer(freelancerShallow(source.getFreelancer()));
        }
        if (source.getProject() != null) {
            target.setProject(projectShallow(source.getProject()));
        }
    }

    @Named("freelancerShallow")
    default Freelancer freelancerShallow(FreelancerJpaModel jpa) {
        if (jpa == null) return null;
        Freelancer f = new Freelancer();
        f.setId(jpa.getId());
        return f;
    }

    @Named("projectShallow")
    default Project projectShallow(ProjectJpaModel jpa) {
        if (jpa == null) return null;
        Project p = new Project();
        p.setId(jpa.getId());
        if (jpa.getContractor() != null) {
            com.ufc.quixada.api.domain.entities.Contractor c = new com.ufc.quixada.api.domain.entities.Contractor();
            c.setId(jpa.getContractor().getId());
            p.setContractor(c);
        }
        return p;
    }

    Propose toDomain(CreateProposeRequestDTO proposeResponseDTO);
    Propose toDomain(AnswerProposeRequestDTO answererProposeRequestDTO);
    ProposeResponseDTO toDTO(Propose propose);
}
