package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Subcategory;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository {
    List<Subcategory> findAll();
    Optional<Subcategory> findById(Long id);
    List<Subcategory> findByCategoryId(Long categoryId);
    Subcategory save(Subcategory subcategory);
    void deleteById(Long id);
}

