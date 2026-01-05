package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    List<File> findAll();
    List<File> findAllById(List<Long> ids);
    Optional<File> findById(Long id);
    File save(File file);
    void deleteById(Long id);
    List<File> saveAll(List<File> files);
}

