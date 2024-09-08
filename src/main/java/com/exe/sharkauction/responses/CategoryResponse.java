package com.exe.sharkauction.responses;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;

    private String name;

    private String imageUrl;

    private List<CategoryResponse> children;
}
