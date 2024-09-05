package com.exe.sharkauction.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest implements Serializable {
    @NotBlank
    private String user_name;

    @NotBlank
    private String password;
}
