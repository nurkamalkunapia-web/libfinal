package com.library.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalBookResponse {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private Integer publishedYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private String coverImagePath;
    private String authorName;
    private String categoryName;
    private LocalDateTime createdAt;
}