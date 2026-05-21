package com.library.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KunapiyaNurkamalAuthorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private String nationality;
    private int bookCount;
}