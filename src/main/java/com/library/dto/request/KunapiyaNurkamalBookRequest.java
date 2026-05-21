package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalBookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private String description;

    @Min(value = 1000, message = "Published year must be valid")
    private Integer publishedYear;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    private Integer totalCopies;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}