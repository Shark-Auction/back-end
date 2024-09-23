package com.exe.sharkauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViolateRequest {
    private long userId;

    private String type;

    private String description;

    private String status;
}
