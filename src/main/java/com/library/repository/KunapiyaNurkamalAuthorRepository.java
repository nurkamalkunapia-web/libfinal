package com.library.repository;

import com.library.entity.KunapiyaNurkamalAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KunapiyaNurkamalAuthorRepository extends JpaRepository<KunapiyaNurkamalAuthor, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}