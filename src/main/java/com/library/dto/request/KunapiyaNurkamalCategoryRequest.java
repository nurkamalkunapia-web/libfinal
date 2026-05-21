package com.library.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}