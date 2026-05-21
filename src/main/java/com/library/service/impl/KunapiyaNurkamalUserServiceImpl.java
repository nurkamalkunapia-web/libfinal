package com.library.service.impl;

import com.library.dto.request.KunapiyaNurkamalAuthRequest;
import com.library.dto.response.KunapiyaNurkamalAuthResponse;
import com.library.dto.response.KunapiyaNurkamalBorrowResponse;
import com.library.entity.KunapiyaNurkamalUser;
import com.library.exception.KunapiyaNurkamalDuplicateResourceException;
import com.library.exception.KunapiyaNurkamalResourceNotFoundException;
import com.library.repository.KunapiyaNurkamalBorrowRecordRepository;
import com.library.repository.KunapiyaNurkamalUserRepository;
import com.library.security.jwt.KunapiyaNurkamalJwtUtil;
import com.library.service.interfaces.KunapiyaNurkamalUserService;
import org.springframework.security.authentication.BadCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KunapiyaNurkamalUserServiceImpl implements KunapiyaNurkamalUserService {

    private final KunapiyaNurkamalUserRepository userRepository;
    private final KunapiyaNurkamalBorrowRecordRepository borrowRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final KunapiyaNurkamalJwtUtil jwtUtil;

    @Override
    public KunapiyaNurkamalAuthResponse.TokenResponse register(KunapiyaNurkamalAuthRequest.RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new KunapiyaNurkamalDuplicateResourceException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new KunapiyaNurkamalDuplicateResourceException("Email already exists: " + request.getEmail());
        }

        KunapiyaNurkamalUser user = KunapiyaNurkamalUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(KunapiyaNurkamalUser.Role.USER)
                .build();

        KunapiyaNurkamalUser saved = userRepository.save(user);
        log.info("User registered successfully: {}", saved.getUsername());

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(saved.getUsername())
                .password(saved.getPassword())
                .authorities("ROLE_" + saved.getRole().name())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        return KunapiyaNurkamalAuthResponse.TokenResponse.builder()
                .token(token)
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .build();
    }

    @Override
    public KunapiyaNurkamalAuthResponse.TokenResponse login(KunapiyaNurkamalAuthRequest.LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        KunapiyaNurkamalUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();

        String token = jwtUtil.generateToken(userDetails);

        log.info("User logged in successfully: {}", user.getUsername());

        return KunapiyaNurkamalAuthResponse.TokenResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public KunapiyaNurkamalUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new KunapiyaNurkamalResourceNotFoundException("Current user not found"));
    }

    @Override
    public Page<KunapiyaNurkamalBorrowResponse> getUserBorrowHistory(Long userId, Pageable pageable) {
        log.debug("Fetching borrow history for user id: {}", userId);

        return borrowRecordRepository.findByUserId(userId, pageable)
                .map(record -> KunapiyaNurkamalBorrowResponse.builder()
                        .id(record.getId())
                        .username(record.getUser().getUsername())
                        .bookTitle(record.getBook().getTitle())
                        .bookIsbn(record.getBook().getIsbn())
                        .borrowDate(record.getBorrowDate())
                        .dueDate(record.getDueDate())
                        .returnDate(record.getReturnDate())
                        .status(record.getStatus().name())
                        .createdAt(record.getCreatedAt())
                        .build());
    }
}