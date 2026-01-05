package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.CategoryMapper;
import com.ufc.quixada.api.domain.entities.Category;
import com.ufc.quixada.api.domain.repositories.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaRepo;
    private final CategoryMapper mapper;

    public CategoryRepositoryImpl(JpaCategoryRepository jpaRepo, CategoryMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(Long id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Category save(Category category) {
        return mapper.toDomain(
                jpaRepo.save(
                        mapper.toJpaEntity(category)
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}
