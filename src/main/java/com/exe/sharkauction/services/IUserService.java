package com.exe.sharkauction.services;

import com.exe.sharkauction.requests.UserForgotPasswordRequest;
import com.exe.sharkauction.requests.UserSignInRequest;
import com.exe.sharkauction.requests.UserSignUpRequest;
import com.exe.sharkauction.responses.SignInResponse;

public interface IUserService {
    void signUp(UserSignUpRequest request);

    void verify(Long userId,String token);

    SignInResponse signIn(UserSignInRequest request);

    String refresh(String token);
    void setPassword(Long userId, String token, UserForgotPasswordRequest request);

    void sendMailForgotPassword(String email);

    void checkUserName(String userName);
    void checkEmail(String email);
}
