package com.library.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private int bookCount;
}