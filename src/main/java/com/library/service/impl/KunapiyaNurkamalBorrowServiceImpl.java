package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalBorrowRequest;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.entity.*;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.repository.*;
import com.library.service.interfaces.KunapiyaNurkamalAsyncService;
import com.library.service.interfaces.KunapiyaNurkamalBorrowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalBorrowServiceImpl implements KunapiyaNurkamalBorrowService {

    private final KunapiyaNurkamalBorrowRecordRepository borrowRecordRepository;
    private final KunapiyaNurkamalBookRepository bookRepository;
    private final KunapiyaNurkamalUserRepository userRepository;
    private final KunapiyaNurkamalFineRepository fineRepository;
    private final KunapiyaNurkamalAsyncService asyncService;
    private final KunapiyaNurkamalUserServiceImpl userService;

    @Override
    public KunapiyaNurkamalBorrowResponse borrowBook(KunapiyaNurkamalBorrowRequest request) {
        log.info("Processing borrow request for book id: {}", request.getBookId());

        KunapiyaNurkamalUser user = userService.getCurrentUser();
        KunapiyaNurkamalBook book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Book not found with id: " + request.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies for book: " + book.getTitle());
        }

        boolean alreadyBorrowed = borrowRecordRepository.existsByUserIdAndBookIdAndStatusNot(
                user.getId(), book.getId(), KunapiyaNurkamalBorrowRecord.BorrowStatus.RETURNED);

        if (alreadyBorrowed) {
            throw new RuntimeException("You have already borrowed this book: " + book.getTitle());
        }

        KunapiyaNurkamalBorrowRecord record = KunapiyaNurkamalBorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(request.getDueDate())
                .status(KunapiyaNurkamalBorrowRecord.BorrowStatus.BORROWED)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        KunapiyaNurkamalBorrowRecord saved = borrowRecordRepository.save(record);
        log.info("Book borrowed successfully: {} by {}", book.getTitle(), user.getUsername());

        asyncService.sendBorrowNotification(user.getUsername(), book.getTitle());

        return buildResponse(saved);
    }

    @Override
    public KunapiyaNurkamalBorrowResponse returnBook(Long borrowRecordId) {
        log.info("Processing return for borrow record id: {}", borrowRecordId);

        KunapiyaNurkamalBorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Borrow record not found with id: " + borrowRecordId));

        if (record.getStatus() == KunapiyaNurkamalBorrowRecord.BorrowStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        record.setReturnDate(LocalDate.now());
        record.setStatus(KunapiyaNurkamalBorrowRecord.BorrowStatus.RETURNED);

        KunapiyaNurkamalBook book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        if (record.getDueDate().isBefore(LocalDate.now())) {
            long daysOverdue = ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());
            BigDecimal fineAmount = BigDecimal.valueOf(daysOverdue * 100);

            KunapiyaNurkamalFine fine = KunapiyaNurkamalFine.builder()
                    .borrowRecord(record)
                    .amount(fineAmount)
                    .paid(false)
                    .build();
            fineRepository.save(fine);
            log.warn("Fine applied for overdue book: {} - Amount: {} tenge", book.getTitle(), fineAmount);
        }

        KunapiyaNurkamalBorrowRecord saved = borrowRecordRepository.save(record);
        log.info("Book returned successfully: {}", book.getTitle());

        return buildResponse(saved);
    }

    @Override
    public Page<KunapiyaNurkamalBorrowResponse> getAllBorrowRecords(Pageable pageable) {
        log.debug("Fetching all borrow records");
        return borrowRecordRepository.findAll(pageable).map(this::buildResponse);
    }

    @Override
    public Page<KunapiyaNurkamalBorrowResponse> getUserBorrowRecords(Long userId, Pageable pageable) {
        log.debug("Fetching borrow records for user id: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new KunapiyaNurkamalResourceNotFoundException("User not found with id: " + userId);
        }

        return borrowRecordRepository.findByUserId(userId, pageable).map(this::buildResponse);
    }

    @Override
    public void checkOverdueBorrows() {
        log.info("Checking for overdue borrows");

        List<KunapiyaNurkamalBorrowRecord> overdue = borrowRecordRepository.findOverdueRecords(LocalDate.now());

        for (KunapiyaNurkamalBorrowRecord record : overdue) {
            if (record.getStatus() == KunapiyaNurkamalBorrowRecord.BorrowStatus.BORROWED) {
                record.setStatus(KunapiyaNurkamalBorrowRecord.BorrowStatus.OVERDUE);
                borrowRecordRepository.save(record);
                log.warn("Overdue book detected: {} borrowed by {}",
                        record.getBook().getTitle(), record.getUser().getUsername());
            }
        }
    }

    private KunapiyaNurkamalBorrowResponse buildResponse(KunapiyaNurkamalBorrowRecord record) {
        return KunapiyaNurkamalBorrowResponse.builder()
                .id(record.getId())
                .username(record.getUser().getUsername())
                .bookTitle(record.getBook().getTitle())
                .bookIsbn(record.getBook().getIsbn())
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus().name())
                .createdAt(record.getCreatedAt())
                .build();
    }
}