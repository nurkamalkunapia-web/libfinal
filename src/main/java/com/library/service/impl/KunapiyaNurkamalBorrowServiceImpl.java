package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalBorrowRequest;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.entity.KunapiyaNurkamalBook;
import com.library.entity.KunapiyaNurkamalBorrowRecord;
import com.library.entity.KunapiyaNurkamalUser;
import com.library.exception.KunapiyaNurkamalDuplicateResourceException;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.mapper.KunapiyaNurkamalMapper;
import com.library.repository.KunapiyaNurkamalBookRepository;
import com.library.repository.KunapiyaNurkamalBorrowRecordRepository;
import com.library.service.interfaces.KunapiyaNurkamalAsyncService;
import com.library.service.interfaces.KunapiyaNurkamalBorrowService;
import com.library.service.interfaces.KunapiyaNurkamalUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalBorrowServiceImpl implements KunapiyaNurkamalBorrowService {

    private final KunapiyaNurkamalBorrowRecordRepository borrowRecordRepository;
    private final KunapiyaNurkamalBookRepository bookRepository;
    private final KunapiyaNurkamalUserService userService;
    private final KunapiyaNurkamalAsyncService asyncService;
    private final KunapiyaNurkamalMapper mapper;

    @Override
    public KunapiyaNurkamalBorrowResponse borrowBook(KunapiyaNurkamalBorrowRequest request) {
        KunapiyaNurkamalUser currentUser = userService.getCurrentUser();

        KunapiyaNurkamalBook book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new KunapiyaNurkamalResourceNotFoundException("No available copies for this book");
        }

        if (borrowRecordRepository.existsByUserIdAndBookIdAndStatus(
                currentUser.getId(), book.getId(), KunapiyaNurkamalBorrowRecord.BorrowStatus.BORROWED)) {
            throw new KunapiyaNurkamalDuplicateResourceException("You already borrowed this book");
        }

        KunapiyaNurkamalBorrowRecord borrowRecord = KunapiyaNurkamalBorrowRecord.builder()
                .user(currentUser)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(request.getDueDate())
                .status(KunapiyaNurkamalBorrowRecord.BorrowStatus.BORROWED)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        KunapiyaNurkamalBorrowRecord saved = borrowRecordRepository.save(borrowRecord);

        asyncService.sendBorrowNotification(currentUser.getUsername(), book.getTitle());

        log.info("Book borrowed: {} by user {}", book.getTitle(), currentUser.getUsername());
        return mapper.toBorrowResponse(saved);
    }

    @Override
    public KunapiyaNurkamalBorrowResponse returnBook(Long borrowRecordId) {
        KunapiyaNurkamalBorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Borrow record not found"));

        if (record.getStatus() == KunapiyaNurkamalBorrowRecord.BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus(KunapiyaNurkamalBorrowRecord.BorrowStatus.RETURNED);

        KunapiyaNurkamalBook book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        KunapiyaNurkamalBorrowRecord updated = borrowRecordRepository.save(record);
        log.info("Book returned: {}", book.getTitle());
        return mapper.toBorrowResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KunapiyaNurkamalBorrowResponse> getAllBorrowRecords(Pageable pageable) {
        return borrowRecordRepository.findAll(pageable).map(mapper::toBorrowResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KunapiyaNurkamalBorrowResponse> getUserBorrowRecords(Long userId, Pageable pageable) {
        return borrowRecordRepository.findByUserId(userId, pageable).map(mapper::toBorrowResponse);
    }

    @Scheduled(cron = "0 0 * * * *") // Каждый час
    @Override
    public void checkOverdueBorrows() {
        log.info("Scheduled task: Checking overdue borrows...");
        asyncService.checkOverdueBorrowsAsync().join();
    }
}