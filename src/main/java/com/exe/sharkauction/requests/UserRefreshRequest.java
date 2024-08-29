package com.exe.sharkauction.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRefreshRequest {
    private String refreshToken;
}
