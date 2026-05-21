package com.library.service.interfaces;

import java.util.concurrent.CompletableFuture;

public interface KunapiyaNurkamalAsyncService {

    CompletableFuture<Void> sendBorrowNotification(String username, String bookTitle);

    CompletableFuture<Void> checkOverdueBorrowsAsync();

    CompletableFuture<Void> logUserAction(String action, String username);
}