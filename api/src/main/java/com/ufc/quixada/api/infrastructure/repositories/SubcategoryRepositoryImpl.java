package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.SubcategoryMapper;
import com.ufc.quixada.api.domain.entities.Subcategory;
import com.ufc.quixada.api.domain.repositories.SubcategoryRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubcategoryRepositoryImpl implements SubcategoryRepository {

    private final JpaSubcategoryRepository jpaRepo;
    private final SubcategoryMapper mapper;

    public SubcategoryRepositoryImpl(JpaSubcategoryRepository jpaRepo, SubcategoryMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subcategory> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subcategory> findById(Long id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subcategory> findByCategoryId(Long categoryId) {
        return jpaRepo.findByCategoryId(categoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Subcategory save(Subcategory subcategory) {
        return mapper.toDomain(
                jpaRepo.save(
                        mapper.toJpaEntity(subcategory)
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }
}

