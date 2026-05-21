package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalBookRequest;
import com.library.dto.response.KunapiyaNurkamalBookResponse;
import com.library.entity.KunapiyaNurkamalAuthor;
import com.library.entity.KunapiyaNurkamalBook;
import com.library.entity.KunapiyaNurkamalCategory;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.mapper.KunapiyaNurkamalMapper;
import com.library.repository.KunapiyaNurkamalAuthorRepository;
import com.library.repository.KunapiyaNurkamalBookRepository;
import com.library.repository.KunapiyaNurkamalCategoryRepository;
import com.library.service.interfaces.KunapiyaNurkamalBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalBookServiceImpl implements KunapiyaNurkamalBookService {

    private final KunapiyaNurkamalBookRepository bookRepository;
    private final KunapiyaNurkamalAuthorRepository authorRepository;
    private final KunapiyaNurkamalCategoryRepository categoryRepository;
    private final KunapiyaNurkamalMapper mapper;

    @Override
    public KunapiyaNurkamalBookResponse createBook(KunapiyaNurkamalBookRequest request) {
        log.info("Creating new book: {}", request.getTitle());

        KunapiyaNurkamalAuthor author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Author not found with id: " + request.getAuthorId()));

        KunapiyaNurkamalCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        KunapiyaNurkamalBook book = mapper.toBook(request);
        book.setAuthor(author);
        book.setCategory(category);
        book.setAvailableCopies(request.getTotalCopies());

        KunapiyaNurkamalBook saved = bookRepository.save(book);
        log.info("Book created successfully with id: {}", saved.getId());

        return mapper.toBookResponse(saved);
    }

    @Override
    public KunapiyaNurkamalBookResponse getBookById(Long id) {
        log.debug("Fetching book with id: {}", id);

        KunapiyaNurkamalBook book = bookRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Book not found with id: " + id));

        return mapper.toBookResponse(book);
    }

    @Override
    public Page<KunapiyaNurkamalBookResponse> getAllBooks(Pageable pageable) {
        log.debug("Fetching all books with pagination");
        return bookRepository.findAll(pageable).map(mapper::toBookResponse);
    }

    @Override
    public Page<KunapiyaNurkamalBookResponse> searchBooks(String title, Long authorId, Long categoryId, Boolean available, Pageable pageable) {
        log.debug("Searching books with filters - title: {}, authorId: {}, categoryId: {}, available: {}", title, authorId, categoryId, available);
        return bookRepository.findWithFilters(title, authorId, categoryId, available, pageable)
                .map(mapper::toBookResponse);
    }

    @Override
    public KunapiyaNurkamalBookResponse updateBook(Long id, KunapiyaNurkamalBookRequest request) {
        log.info("Updating book with id: {}", id);

        KunapiyaNurkamalBook book = bookRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Book not found with id: " + id));

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPublishedYear(request.getPublishedYear());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());

        if (!request.getAuthorId().equals(book.getAuthor().getId())) {
            KunapiyaNurkamalAuthor author = authorRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Author not found with id: " + request.getAuthorId()));
            book.setAuthor(author);
        }

        if (!request.getCategoryId().equals(book.getCategory().getId())) {
            KunapiyaNurkamalCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            book.setCategory(category);
        }

        KunapiyaNurkamalBook updated = bookRepository.save(book);
        log.info("Book updated successfully with id: {}", updated.getId());

        return mapper.toBookResponse(updated);
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        if (!bookRepository.existsById(id)) {
            throw new KunapiyaNurkamalResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book deleted successfully with id: {}", id);
    }
}