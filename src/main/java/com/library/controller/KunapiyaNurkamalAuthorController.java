package com.library.controller;

import com.library.dto.request.KunapiyaNurkamalAuthorRequest;
import com.library.dto.response.KunapiyaNurkamalAuthorResponse;
import com.library.service.interfaces.KunapiyaNurkamalAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Author management endpoints")
public class KunapiyaNurkamalAuthorController {

    private final KunapiyaNurkamalAuthorService authorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new author")
    public ResponseEntity<KunapiyaNurkamalAuthorResponse> createAuthor(
            @Valid @RequestBody KunapiyaNurkamalAuthorRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID")
    public ResponseEntity<KunapiyaNurkamalAuthorResponse> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping
    @Operation(summary = "Get all authors with pagination")
    public ResponseEntity<Page<KunapiyaNurkamalAuthorResponse>> getAllAuthors(Pageable pageable) {
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update author")
    public ResponseEntity<KunapiyaNurkamalAuthorResponse> updateAuthor(
            @PathVariable Long id,
            @Valid @RequestBody KunapiyaNurkamalAuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete author")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}