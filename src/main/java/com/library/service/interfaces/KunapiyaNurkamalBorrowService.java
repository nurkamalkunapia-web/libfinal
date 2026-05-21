package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalBorrowRequest;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KunapiyaNurkamalBorrowService {

    KunapiyaNurkamalBorrowResponse borrowBook(KunapiyaNurkamalBorrowRequest request);

    KunapiyaNurkamalBorrowResponse returnBook(Long borrowRecordId);

    Page<KunapiyaNurkamalBorrowResponse> getAllBorrowRecords(Pageable pageable);

    Page<KunapiyaNurkamalBorrowResponse> getUserBorrowRecords(Long userId, Pageable pageable);

    void checkOverdueBorrows();
}