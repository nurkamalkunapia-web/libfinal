package com.library.controller;

import com.library.dto.request.KunapiyaNurkamalAuthRequest;
import com.library.dto.response.KunapiyaNurkamalAuthResponse;
import com.library.service.interfaces.KunapiyaNurkamalUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login and Registration endpoints")
public class KunapiyaNurkamalAuthController {

    private final KunapiyaNurkamalUserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns JWT token")
    public ResponseEntity<KunapiyaNurkamalAuthResponse.TokenResponse> register(
            @Valid @RequestBody KunapiyaNurkamalAuthRequest.RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    public ResponseEntity<KunapiyaNurkamalAuthResponse.TokenResponse> login(
            @Valid @RequestBody KunapiyaNurkamalAuthRequest.LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}