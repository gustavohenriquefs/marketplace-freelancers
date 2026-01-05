package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.Skill;
import com.ufc.quixada.api.infrastructure.models.SkillJpaModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillMapper INSTANCE = Mappers.getMapper(SkillMapper.class);

    Skill toDomain(SkillJpaModel jpaEntity);
    SkillJpaModel toJpaEntity(Skill domain);
}
