package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalCategoryRequest;
import com.library.dto.response.KunapiyaNurkamalCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KunapiyaNurkamalCategoryService {

    KunapiyaNurkamalCategoryResponse createCategory(KunapiyaNurkamalCategoryRequest request);

    KunapiyaNurkamalCategoryResponse getCategoryById(Long id);

    Page<KunapiyaNurkamalCategoryResponse> getAllCategories(Pageable pageable);

    KunapiyaNurkamalCategoryResponse updateCategory(Long id, KunapiyaNurkamalCategoryRequest request);

    void deleteCategory(Long id);
}