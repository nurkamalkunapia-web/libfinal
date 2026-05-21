package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalCategoryRequest;
import com.library.dto.response.KunapiyaNurkamalCategoryResponse;

import java.util.List;

public interface KunapiyaNurkamalCategoryService {
    KunapiyaNurkamalCategoryResponse createCategory(KunapiyaNurkamalCategoryRequest request);

    KunapiyaNurkamalCategoryResponse getCategoryById(Long id);

    List<KunapiyaNurkamalCategoryResponse> getAllCategories();

    KunapiyaNurkamalCategoryResponse updateCategory(Long id, KunapiyaNurkamalCategoryRequest request);

    void deleteCategory(Long id);
}