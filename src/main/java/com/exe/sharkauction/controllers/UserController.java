package com.exe.sharkauction.controllers;


import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.components.configurations.AppProperties;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.mappers.IUserMapper;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.requests.*;
import com.exe.sharkauction.responses.RefreshResponse;
import com.exe.sharkauction.responses.SignInResponse;
import com.exe.sharkauction.responses.UserProfileResponse;
import com.exe.sharkauction.services.IUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.exe.sharkauction.mappers.IUserMapper.INSTANCE;


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
        return CoreApiResponse.success("Tài khoản đã được tạo thành công. Vui lòng kiểm tra email để xác thực ");
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
        return CoreApiResponse.success("Xác minh email thành công");
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
        return CoreApiResponse.success("Vui lòng kiểm tra email để thay đổi mật khẩu");
    }

    @PutMapping("/setpassword")
    public CoreApiResponse<?> setPassword(
            @RequestParam("userId") Long userId,
            @RequestParam("token") String token,
            @RequestBody UserForgotPasswordRequest request
    ){
        userService.setPassword(userId, token, request);
        return CoreApiResponse.success("Thay đổi mật khẩu thành công");
    }

    @PutMapping("/changepassword")
    public CoreApiResponse<?> changePassword(
            @RequestBody UserChangePasswordRequest request
    ){
        userService.changePassword(request);
        return CoreApiResponse.success("Thay đổi mật khẩu thành công");
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
        return CoreApiResponse.success("Tên người dùng đã tồn tại.");
    }

    @GetMapping("/check-email")
    public CoreApiResponse<?> checkEmail(
            @RequestParam String email
    ) {
        userService.checkEmail(email);
        return CoreApiResponse.success("Email đã tồn tại.");
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public CoreApiResponse<UserProfileResponse> getCurrentUserProfile() {
        UserEntity currentUser = userService.getUserProfile();
        UserProfileResponse response =INSTANCE.toResponse(currentUser);

        return CoreApiResponse.success(response);
    }
}