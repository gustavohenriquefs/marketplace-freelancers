package com.ufc.quixada.api.application.mappers;

import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
//import com.ufc.quixada.api.presentation.dtos.UserRequestDTO;
//import com.ufc.quixada.api.presentation.dtos.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
        FreelancerMapper.class,
        ContractorMapper.class
})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toDomain(UserJpaModel jpaEntity);
    //User toDomain(UserRequestDTO dto);
    UserJpaModel toJpaEntity(User domain);

    //@Mapping(target = "id", source = "user.id")
    //@Mapping(target = "name", source = "user.name")
    //@Mapping(target = "email", source = "user.email")
    //UserResponseDTO toDTO(User domain);
}