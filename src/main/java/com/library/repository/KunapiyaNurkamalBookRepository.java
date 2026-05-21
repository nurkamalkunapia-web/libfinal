package com.library.repository;

import com.library.entity.KunapiyaNurkamalBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KunapiyaNurkamalBookRepository extends JpaRepository<KunapiyaNurkamalBook, Long> {

    Optional<KunapiyaNurkamalBook> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query("SELECT b FROM KunapiyaNurkamalBook b WHERE " +
            "(:title IS NULL OR b.title LIKE %:title%) AND " +
            "(:authorId IS NULL OR b.author.id = :authorId) AND " +
            "(:categoryId IS NULL OR b.category.id = :categoryId) AND " +
            "(:available IS NULL OR (:available = true AND b.availableCopies > 0) OR (:available = false AND b.availableCopies = 0))")
    Page<KunapiyaNurkamalBook> findWithFilters(
            @Param("title") String title,
            @Param("authorId") Long authorId,
            @Param("categoryId") Long categoryId,
            @Param("available") Boolean available,
            Pageable pageable);
}