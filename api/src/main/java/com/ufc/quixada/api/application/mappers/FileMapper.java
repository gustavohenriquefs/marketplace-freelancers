package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.File;
import com.ufc.quixada.api.infrastructure.models.FileJpaModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    File toDomain(FileJpaModel jpaEntity);

    @Mapping(target = "project", ignore = true)
    FileJpaModel toJpaEntity(File domain);
}

