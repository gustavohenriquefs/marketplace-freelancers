package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillRepository {
    List<Skill> findAll();
    Optional<Skill> findById(Long id);
    Skill save(Skill skill);
    void deleteById(Long id);
    List<Skill> findAllById(List<Long> ids);
}

