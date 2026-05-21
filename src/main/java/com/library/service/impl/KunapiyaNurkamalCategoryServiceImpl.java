package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalCategoryRequest;
import com.library.dto.response.KunapiyaNurkamalCategoryResponse;
import com.library.entity.KunapiyaNurkamalCategory;
import com.library.exception.KunapiyaNurkamalDuplicateResourceException;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.mapper.KunapiyaNurkamalMapper;
import com.library.repository.KunapiyaNurkamalCategoryRepository;
import com.library.service.interfaces.KunapiyaNurkamalCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalCategoryServiceImpl implements KunapiyaNurkamalCategoryService {

    private final KunapiyaNurkamalCategoryRepository categoryRepository;
    private final KunapiyaNurkamalMapper mapper;

    @Override
    public KunapiyaNurkamalCategoryResponse createCategory(KunapiyaNurkamalCategoryRequest request) {
        log.info("Creating new category: {}", request.getName());

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new KunapiyaNurkamalDuplicateResourceException("Category already exists with name: " + request.getName());
        }

        KunapiyaNurkamalCategory category = mapper.toCategory(request);
        KunapiyaNurkamalCategory saved = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", saved.getId());

        return mapper.toCategoryResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public KunapiyaNurkamalCategoryResponse getCategoryById(Long id) {
        log.debug("Fetching category with id: {}", id);

        KunapiyaNurkamalCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id));

        return mapper.toCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KunapiyaNurkamalCategoryResponse> getAllCategories() {
        log.debug("Fetching all categories");
        List<KunapiyaNurkamalCategory> categories = categoryRepository.findAll();
        return mapper.toCategoryResponseList(categories);
    }

    @Override
    public KunapiyaNurkamalCategoryResponse updateCategory(Long id, KunapiyaNurkamalCategoryRequest request) {
        log.info("Updating category with id: {}", id);

        KunapiyaNurkamalCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.findByName(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new KunapiyaNurkamalDuplicateResourceException("Category already exists with name: " + request.getName());
                    }
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        KunapiyaNurkamalCategory updated = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", updated.getId());

        return mapper.toCategoryResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with id: {}", id);

        KunapiyaNurkamalCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id));

        if (category.getBooks() != null && !category.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete category as it contains books");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {}", id);
    }
}