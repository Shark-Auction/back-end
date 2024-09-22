package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.events.MailEvent;
import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.components.securities.TokenProvider;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.TokenEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.repositories.IRoleRepository;
import com.exe.sharkauction.repositories.ITokenRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.requests.UserChangePasswordRequest;
import com.exe.sharkauction.requests.UserForgotPasswordRequest;
import com.exe.sharkauction.requests.UserSignInRequest;
import com.exe.sharkauction.requests.UserSignUpRequest;
import com.exe.sharkauction.responses.SignInResponse;
import com.exe.sharkauction.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final ITokenRepository tokenRepository;
    private final TokenProvider tokenProvider;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    AuthenticationManager authenticationManager;

    @Value("${app.fe.verify_url}")
    private String verifyUrl;

    @Value("${app.fe.forgot_password_url}")
    private String forgotPasswordUrl;


    @Override
    public void signUp(UserSignUpRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        if(userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            if(user.getEmail_verified()){
                throw new AppException(HttpStatus.BAD_REQUEST,"Email address already in use.");
            }else{
                sendVerifyMail(user);
                return;
            }
        }
        UserEntity user = new UserEntity();
        user.setFull_name(request.getFullName());
        user.setUser_name(request.getUserName());
        user.setPhone_number(request.getPhone());
        user.setAddress(request.getAddress());
        user.setDate_of_birth(request.getDob()  );
        user.setEmail(request.getEmail());
        user.setRole_id(roleRepository.findById(4L)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This role is not existed!")));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail_verified(false);

        userRepository.save(user);

        sendVerifyMail(user);
    }

    @Override
    public SignInResponse signIn(UserSignInRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUser_name(),
                        request.getPassword()
                )
        );
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if(!userPrincipal.getUser().getEmail_verified()){
            sendVerifyMail(userPrincipal.getUser());
            throw new AppException(HttpStatus.BAD_REQUEST,"Email not verified");
        }
        if (!userPrincipal.getUser().getIs_active()) {
            throw new AppException(HttpStatus.FORBIDDEN, "Account is deactivated");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userPrincipal.getId())
                .fullName(userPrincipal.getFullName())
                .userName(userPrincipal.getUsername())
                .roleName(userPrincipal.getUser().getRole_id().getName())
                .build();
    }

    @Override
    public void verify(Long userId, String token) {
        tokenProvider.validateToken(token);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with id : " + userId)
                );
        user.setEmail_verified(true);

        userRepository.save(user);
    }

    @Override
    public String refresh(String token) {
        TokenEntity refreshToken = tokenRepository.findByName(token)
                .orElseThrow(() ->
                        new AppException(HttpStatus.BAD_REQUEST,"Refresh Token not found with token : " + token)
                );
        if(refreshToken.isRevoked()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã bị thu hồi");
        }
        if(refreshToken.isExpired()){
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }
        if(refreshToken.getExpiration_date().isBefore(LocalDate.now())){
            refreshToken.setExpired(true);
            throw new AppException(HttpStatus.BAD_REQUEST,"Token đã hết hạn");
        }

        String accessToken = tokenProvider.createAccessToken(refreshToken.getUser().getId());

        return accessToken;
    }

    @Override
    public void setPassword(Long userId, String token, UserForgotPasswordRequest request) {
        tokenProvider.validateToken(token);
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", userId));
        if(request.getPassword().equals(request.getConfirmedPassword())){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        else{
            throw new AppException(HttpStatus.BAD_REQUEST, "Confirmed password is wrong");
        }
        userRepository.save(user);
    }

    @Override
    public void sendMailForgotPassword(String email) {
        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User", "Email", email));

        String token = tokenProvider.createToken(user.getId(), 600000); //10 minutes    `
        String urlPattern = forgotPasswordUrl + "?userId={0}&token={1}";
        String url = MessageFormat.format(urlPattern, user.getId(), token);
        applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "forgot"));
    }

    public void checkUserName(String userName) {
        boolean exists = userRepository.existsByUserName(userName);
        if (exists) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }
    }

    public void checkEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        if (exists) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Email already exists.");
        }
    }

    private void sendVerifyMail(UserEntity user) {
        String token = tokenProvider.createToken(user.getId(), 600000); // 10 minutes
        String urlPattern = verifyUrl + "?userId={0}&token={1}";
        String url = MessageFormat.format(urlPattern, user.getId(), token);
        applicationEventPublisher.publishEvent(new MailEvent(this, user, url, "verify"));
    }

    @Override
    public UserEntity getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();
        return user;
    }
    @Override
    public void changePassword(UserChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserEntity user = userPrincipal.getUser();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu cũ không chính xác");
        }

        if (!request.getNewPassword().equals(request.getConfirmedPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu mới và mật khẩu xác nhận không khớp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }



}
