package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Category;
import com.ufc.quixada.api.domain.entities.Subcategory;
import com.ufc.quixada.api.infrastructure.models.CategoryJpaEntity;
import com.ufc.quixada.api.infrastructure.models.SubcategoryJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubcategoryMapper {
    SubcategoryJpaEntity toJpaEntity(Subcategory subcategory);
}
