package com.library.controller;

import com.library.dto.request.KunapiyaNurkamalBookRequest;
import com.library.dto.response.KunapiyaNurkamalBookResponse;
import com.library.service.interfaces.KunapiyaNurkamalBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management endpoints")
public class KunapiyaNurkamalBookController {

    private final KunapiyaNurkamalBookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Supports pagination, sorting, search and filtering")
    public ResponseEntity<Page<KunapiyaNurkamalBookResponse>> getAllBooks(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search by title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by author ID") @RequestParam(required = false) Long authorId,
            @Parameter(description = "Filter by category ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Filter by availability") @RequestParam(required = false) Boolean available
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<KunapiyaNurkamalBookResponse> books = bookService.searchBooks(
                title, authorId, categoryId, available, pageable);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Returns a single book")
    public ResponseEntity<KunapiyaNurkamalBookResponse> getBookById(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new book", description = "Admin only")
    public ResponseEntity<KunapiyaNurkamalBookResponse> createBook(
            @Valid @RequestBody KunapiyaNurkamalBookRequest request) {
        return ResponseEntity.ok(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book", description = "Admin only")
    public ResponseEntity<KunapiyaNurkamalBookResponse> updateBook(
            @Parameter(description = "Book ID") @PathVariable Long id,
            @Valid @RequestBody KunapiyaNurkamalBookRequest request) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book", description = "Admin only")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}