package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Category;
import com.ufc.quixada.api.infrastructure.models.CategoryJpaModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toDomain(CategoryJpaModel jpaEntity);
    CategoryJpaModel toJpaEntity(Category domain);
}
