package com.library.dto.response;

import lombok.*;

public class KunapiyaNurkamalAuthResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse {
        private String token;
        private String username;
        private String role;
    }
}