package com.library.service.interfaces;

import com.library.dto.request.KunapiyaNurkamalAuthRequest;
import com.library.dto.response.KunapiyaNurkamalAuthResponse;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.entity.KunapiyaNurkamalUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KunapiyaNurkamalUserService {

    KunapiyaNurkamalAuthResponse.TokenResponse register(KunapiyaNurkamalAuthRequest.RegisterRequest request);

    KunapiyaNurkamalAuthResponse.TokenResponse login(KunapiyaNurkamalAuthRequest.LoginRequest request);

    KunapiyaNurkamalUser getCurrentUser();

    Page<KunapiyaNurkamalBorrowResponse> getUserBorrowHistory(Long userId, Pageable pageable);
}