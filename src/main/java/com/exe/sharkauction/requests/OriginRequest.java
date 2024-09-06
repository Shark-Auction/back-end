package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OriginRequest {
    @NotBlank(message = "Origin name cannot be empty")
    private String name;
}
