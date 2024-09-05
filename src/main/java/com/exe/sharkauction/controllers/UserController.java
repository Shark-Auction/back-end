package com.exe.sharkauction.controllers;


import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.components.configurations.AppProperties;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.requests.UserForgotPasswordRequest;
import com.exe.sharkauction.requests.UserRefreshRequest;
import com.exe.sharkauction.requests.UserSignInRequest;
import com.exe.sharkauction.requests.UserSignUpRequest;
import com.exe.sharkauction.responses.RefreshResponse;
import com.exe.sharkauction.responses.SignInResponse;
import com.exe.sharkauction.services.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${app.api.version.v1}/user")
public class UserController {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private IUserService userService;

    @PostMapping("/signup")
    public CoreApiResponse<?> signup(@Valid @RequestBody UserSignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
        return CoreApiResponse.success("User registered successfully");
    }

    @PostMapping("/signin")
    public CoreApiResponse<SignInResponse> signin(@Valid @RequestBody UserSignInRequest request, HttpServletResponse response) {
        SignInResponse signIn = userService.signIn(request);
        Cookie cookie = new Cookie("refreshToken", signIn.getRefreshToken());

        // expires in 15 minutes
        cookie.setMaxAge(appProperties.getAuth().getRefreshTokenExpirationMsec());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return CoreApiResponse.success(signIn);
    }

    @GetMapping("/verify")
    public CoreApiResponse<?> verify(
            @RequestParam Long userId,
            @RequestParam String token
    ) {
        userService.verify(userId,token);
        return CoreApiResponse.success("User verified successfully");
    }

    @PostMapping("/refresh")
    public CoreApiResponse<?> refresh(
            @CookieValue(value = "refreshToken", required = false) String cookieRT,
            @RequestBody UserRefreshRequest bodyRT
    ) {
        if(bodyRT == null && !isValidToken(cookieRT)){
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid token");
        }
        String token = bodyRT != null ? bodyRT.getRefreshToken() : cookieRT;

        String accessToken = userService.refresh(token);

        return CoreApiResponse.success(new RefreshResponse(accessToken),"User refresh token successfully");
    }

    @GetMapping("/forgotpassword")
    public CoreApiResponse<?> forgotPassword(
            @Valid @RequestParam String email
    ){
        userService.sendMailForgotPassword(email);
        return CoreApiResponse.success("Check your mail");
    }

    @PutMapping("/setpassword")
    public CoreApiResponse<?> setPassword(
            @RequestParam("userId") Long userId,
            @RequestParam("token") String token,
            @RequestBody UserForgotPasswordRequest request
    ){
        userService.setPassword(userId, token, request);
        return CoreApiResponse.success("Successfully!");
    }

    private boolean isValidToken(String token) {
        return token != null && isJWT(token);
    }
    private boolean isJWT(String token) {
        String[] parts = token.split("\\.");
        return parts.length == 3;
    }

    @GetMapping("/check-username")
    public CoreApiResponse<?> checkUserName(
            @RequestParam String userName
    ) {
        userService.checkUserName(userName);
        return CoreApiResponse.success("Username is available.");
    }

    @GetMapping("/check-email")
    public CoreApiResponse<?> checkEmail(
            @RequestParam String email
    ) {
        userService.checkEmail(email);
        return CoreApiResponse.success("Email is available.");
    }
}