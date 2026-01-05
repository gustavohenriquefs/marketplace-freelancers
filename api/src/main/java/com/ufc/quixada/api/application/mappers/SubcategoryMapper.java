package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Subcategory;
import com.ufc.quixada.api.infrastructure.models.SubcategoryJpaModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryJpaModel toJpaEntity(Subcategory subcategory);
}
