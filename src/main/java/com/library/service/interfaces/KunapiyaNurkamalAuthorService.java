package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalAuthorRequest;
import com.library.dto.response.KunapiyaNurkamalAuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KunapiyaNurkamalAuthorService {

    KunapiyaNurkamalAuthorResponse createAuthor(KunapiyaNurkamalAuthorRequest request);

    KunapiyaNurkamalAuthorResponse getAuthorById(Long id);

    Page<KunapiyaNurkamalAuthorResponse> getAllAuthors(Pageable pageable);

    KunapiyaNurkamalAuthorResponse updateAuthor(Long id, KunapiyaNurkamalAuthorRequest request);

    void deleteAuthor(Long id);
}