package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.FileMapper;
import com.ufc.quixada.api.domain.entities.File;
import com.ufc.quixada.api.domain.repositories.FileRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileRepositoryImpl implements FileRepository {

    private final JpaFileRepository jpaRepo;
    private final FileMapper mapper;

    public FileRepositoryImpl(JpaFileRepository jpaRepo, FileMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<File> findAllById(List<Long> ids) {
        return jpaRepo.findAllById(ids).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<File> findById(Long id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public File save(File file) {
        return mapper.toDomain(
                jpaRepo.save(
                        mapper.toJpaEntity(file)
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }

    @Override
    @Transactional
    public List<File> saveAll(List<File> files) {
        return jpaRepo.saveAll(
                files.stream()
                        .map(mapper::toJpaEntity)
                        .collect(Collectors.toList())
        ).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

