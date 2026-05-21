package com.library.repository;

import com.library.entity.KunapiyaNurkamalFine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KunapiyaNurkamalFineRepository extends JpaRepository<KunapiyaNurkamalFine, Long> {
    List<KunapiyaNurkamalFine> findByBorrowRecordUserId(Long userId);
    List<KunapiyaNurkamalFine> findByPaidFalse();
}