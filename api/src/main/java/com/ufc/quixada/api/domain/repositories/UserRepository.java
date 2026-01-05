package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(String email);
    Optional<User> findByEmail(String email);
}
