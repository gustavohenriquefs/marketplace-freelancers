package com.ufc.quixada.api.domain.repositories;
import java.util.Optional;
import java.util.List;

import com.ufc.quixada.api.domain.entities.Category;

public interface CategoryRepository {
    void deleteById(Long id);
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
}


