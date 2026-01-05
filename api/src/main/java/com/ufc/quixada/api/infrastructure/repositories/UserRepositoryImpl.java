package com.ufc.quixada.api.infrastructure.repositories;


import com.ufc.quixada.api.application.mappers.UserMapper;
import com.ufc.quixada.api.domain.entities.User;
import com.ufc.quixada.api.domain.repositories.UserRepository;
import com.ufc.quixada.api.infrastructure.models.UserJpaModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository springRepo;
    private final UserMapper mapper; // MapStruct ou manual

    public UserRepositoryImpl(UserJpaRepository springRepo, UserMapper mapper) {
        this.springRepo = springRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserJpaModel model = mapper.toJpaEntity(user);
        UserJpaModel saved = springRepo.save(model);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(String email) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return springRepo.findByEmail(email).map(mapper::toDomain);
    }
}
