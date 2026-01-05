package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.presentation.dtos.ProjectRequestDTO;
import com.ufc.quixada.api.presentation.dtos.ProjectResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
        componentModel = "spring",
        uses = {
                ContractorMapper.class,
                SubcategoryMapper.class,
                ProposeMapper.class,
        }
)
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    public Project toDomain(ProjectRequestDTO dto);
    public ProjectResponseDTO toDto(Project project);
    public ProjectJpaModel toJpaEntity(Project project);
    public Project toDomain(ProjectJpaModel jpaEntity);
}
