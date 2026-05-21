package com.library.controller;

import com.library.dto.request.KunapiyaNurkamalBorrowRequest;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.service.interfaces.KunapiyaNurkamalBorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
@Tag(name = "Borrow Records", description = "Book borrowing and returning endpoints")
public class KunapiyaNurkamalBorrowController {

    private final KunapiyaNurkamalBorrowService borrowService;

    @PostMapping("/borrow")
    @Operation(summary = "Borrow a book", description = "Creates a new borrow record")
    public ResponseEntity<KunapiyaNurkamalBorrowResponse> borrowBook(
            @Valid @RequestBody KunapiyaNurkamalBorrowRequest request) {
        return ResponseEntity.ok(borrowService.borrowBook(request));
    }

    @PostMapping("/return/{borrowRecordId}")
    @Operation(summary = "Return a book", description = "Returns a borrowed book and calculates fine if overdue")
    public ResponseEntity<KunapiyaNurkamalBorrowResponse> returnBook(
            @Parameter(description = "Borrow record ID") @PathVariable Long borrowRecordId) {
        return ResponseEntity.ok(borrowService.returnBook(borrowRecordId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all borrow records", description = "Admin only - returns all borrow records with pagination")
    public ResponseEntity<Page<KunapiyaNurkamalBorrowResponse>> getAllBorrows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(borrowService.getAllBorrowRecords(pageable));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user borrow history", description = "Returns borrow history for a specific user")
    public ResponseEntity<Page<KunapiyaNurkamalBorrowResponse>> getUserBorrows(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(borrowService.getUserBorrowRecords(userId, pageable));
    }
}