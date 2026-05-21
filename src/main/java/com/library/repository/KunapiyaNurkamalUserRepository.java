package com.library.repository;

import com.library.entity.KunapiyaNurkamalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KunapiyaNurkamalUserRepository extends JpaRepository<KunapiyaNurkamalUser, Long> {
    Optional<KunapiyaNurkamalUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}