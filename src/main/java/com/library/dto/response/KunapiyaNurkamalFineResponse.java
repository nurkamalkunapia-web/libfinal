package com.library.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalFineResponse {
    private Long id;
    private Long borrowRecordId;
    private String username;
    private String bookTitle;
    private BigDecimal amount;
    private boolean paid;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}