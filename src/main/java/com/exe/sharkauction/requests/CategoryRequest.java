package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "Name can not be empty")
    private String name;

    private long parent_id;
}
