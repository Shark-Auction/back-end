package com.exe.sharkauction.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private String full_name;
    private String user_name;
    private String phone_number;
    private String email;
    private String address;
    private String imageUrl;
}
