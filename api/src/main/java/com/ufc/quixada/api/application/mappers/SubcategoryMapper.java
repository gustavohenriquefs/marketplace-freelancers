package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Subcategory;
import com.ufc.quixada.api.infrastructure.models.SubcategoryJpaModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface SubcategoryMapper {
    SubcategoryMapper INSTANCE = Mappers.getMapper(SubcategoryMapper.class);

    Subcategory toDomain(SubcategoryJpaModel jpaEntity);
    SubcategoryJpaModel toJpaEntity(Subcategory domain);
}

