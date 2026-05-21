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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalCategoryServiceImpl implements KunapiyaNurkamalCategoryService {

    private final KunapiyaNurkamalCategoryRepository categoryRepository;
    private final KunapiyaNurkamalMapper mapper;

    @Override
    public KunapiyaNurkamalCategoryResponse createCategory(KunapiyaNurkamalCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new KunapiyaNurkamalDuplicateResourceException("Category already exists: " + request.getName());
        }

        KunapiyaNurkamalCategory category = mapper.toCategory(request);
        KunapiyaNurkamalCategory saved = categoryRepository.save(category);
        log.info("Category created with id: {}", saved.getId());
        return mapper.toCategoryResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public KunapiyaNurkamalCategoryResponse getCategoryById(Long id) {
        KunapiyaNurkamalCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id));
        return mapper.toCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KunapiyaNurkamalCategoryResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(mapper::toCategoryResponse);
    }

    @Override
    public KunapiyaNurkamalCategoryResponse updateCategory(Long id, KunapiyaNurkamalCategoryRequest request) {
        KunapiyaNurkamalCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id));

        if (!category.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new KunapiyaNurkamalDuplicateResourceException("Category name already exists: " + request.getName());
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        KunapiyaNurkamalCategory updated = categoryRepository.save(category);
        return mapper.toCategoryResponse(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new KunapiyaNurkamalResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}