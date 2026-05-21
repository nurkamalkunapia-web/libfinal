package com.library.service.impl;

import com.library.service.interfaces.KunapiyaNurkamalAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class KunapiyaNurkamalAsyncServiceImpl implements KunapiyaNurkamalAsyncService {

    @Override
    @Async
    public CompletableFuture<Void> sendBorrowNotification(String username, String bookTitle) {
        log.info("Sending borrow notification asynchronously to: {}", username);

        try {
            Thread.sleep(2000);
            log.info("📧 NOTIFICATION: Dear {}, you have successfully borrowed '{}'", username, bookTitle);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Notification sending failed: {}", e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> checkOverdueBorrowsAsync() {
        log.info("Running async overdue borrows check");

        try {
            Thread.sleep(3000);
            log.info("✅ Async overdue check completed successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Overdue check failed: {}", e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> logUserAction(String action, String username) {
        log.info("📝 USER ACTION LOG: [{}] - {}", username, action);
        return CompletableFuture.completedFuture(null);
    }
}