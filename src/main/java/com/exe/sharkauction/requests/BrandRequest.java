package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    @NotBlank(message = "Brand name cannot be empty")
    private String name;
}
