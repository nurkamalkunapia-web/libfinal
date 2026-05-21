package com.library.repository;

import com.library.entity.KunapiyaNurkamalBorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface KunapiyaNurkamalBorrowRecordRepository extends JpaRepository<KunapiyaNurkamalBorrowRecord, Long> {

    Page<KunapiyaNurkamalBorrowRecord> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT b FROM KunapiyaNurkamalBorrowRecord b WHERE b.dueDate < :today AND b.status = 'BORROWED'")
    List<KunapiyaNurkamalBorrowRecord> findOverdueRecords(LocalDate today);

    boolean existsByUserIdAndBookIdAndStatusNot(Long userId, Long bookId, KunapiyaNurkamalBorrowRecord.BorrowStatus status);
}