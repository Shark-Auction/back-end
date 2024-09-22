package com.exe.sharkauction.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {
    @NotEmpty(message = "Vui lòng nhập mật khẩu cũ")
    private String oldPassword;

    @NotEmpty(message = "Vui lòng nhập mật khẩu mới")
    private String newPassword;

    @NotEmpty(message = "Vui lòng nhập lại mật khẩu mới")
    private String confirmedPassword;
}
