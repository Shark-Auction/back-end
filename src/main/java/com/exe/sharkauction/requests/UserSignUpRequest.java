package com.exe.sharkauction.requests;

import com.exe.sharkauction.components.validations.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "confirmPassword", second = "password", message = "Password does not match")
public class UserSignUpRequest {
    @NotEmpty(message = "Full Name is required")
    private String fullName;

    @NotEmpty(message = "User Name is required")
    private String userName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must have 10 number")
    private String phone;

    @NotEmpty(message = "Address is required")
    private String address;

    private LocalDate dob;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;
}
