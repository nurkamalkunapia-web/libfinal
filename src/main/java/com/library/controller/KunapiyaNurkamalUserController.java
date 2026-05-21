package com.library.controller;

import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.service.interfaces.KunapiyaNurkamalUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class KunapiyaNurkamalUserController {

    private final KunapiyaNurkamalUserService userService;

    @GetMapping("/me/borrows")
    @Operation(summary = "Get current user borrow history")
    public ResponseEntity<Page<KunapiyaNurkamalBorrowResponse>> getMyBorrows(Pageable pageable) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(userService.getUserBorrowHistory(userId, pageable));
    }

    @GetMapping("/{userId}/borrows")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user borrow history (Admin only)")
    public ResponseEntity<Page<KunapiyaNurkamalBorrowResponse>> getUserBorrows(
            @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(userService.getUserBorrowHistory(userId, pageable));
    }
}