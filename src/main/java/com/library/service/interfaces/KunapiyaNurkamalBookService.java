package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalBookRequest;
import com.library.dto.response.KunapiyaNurkamalBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KunapiyaNurkamalBookService {

    KunapiyaNurkamalBookResponse createBook(KunapiyaNurkamalBookRequest request);

    KunapiyaNurkamalBookResponse getBookById(Long id);

    Page<KunapiyaNurkamalBookResponse> getAllBooks(Pageable pageable);

    Page<KunapiyaNurkamalBookResponse> searchBooks(String title, Long authorId, Long categoryId, Boolean available, Pageable pageable);

    KunapiyaNurkamalBookResponse updateBook(Long id, KunapiyaNurkamalBookRequest request);

    void deleteBook(Long id);
}