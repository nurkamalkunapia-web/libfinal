package com.library.repository;

import com.library.entity.KunapiyaNurkamalCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KunapiyaNurkamalCategoryRepository extends JpaRepository<KunapiyaNurkamalCategory, Long> {
    Optional<KunapiyaNurkamalCategory> findByName(String name);
    boolean existsByName(String name);
}