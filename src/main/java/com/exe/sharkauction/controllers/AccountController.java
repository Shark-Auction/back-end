package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.RoleEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.requests.CreateAccountRequest;
import com.exe.sharkauction.requests.UpdateUserRequest;
import com.exe.sharkauction.services.IAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.exe.sharkauction.mappers.IUserMapper.INSTANCE;

import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;

    @PostMapping("/users/manager")
    public CoreApiResponse<?> createAccountManager(
            @Valid @RequestBody CreateAccountRequest accountRequest
    ){
        UserEntity user = accountService.createAccountManager(INSTANCE.toModel(accountRequest));
        return CoreApiResponse.success("Insert manager successfully");
    }

    @PostMapping("/users/shipper")
    public CoreApiResponse<?> createAccountShipper(
            @Valid @RequestBody CreateAccountRequest accountRequest
    ){
        UserEntity user = accountService.createAccountShipper(INSTANCE.toModel(accountRequest));
        return CoreApiResponse.success("Insert manager successfully");
    }

    @PostMapping("/users/staff")
    public CoreApiResponse<?> createAccountStaff(
            @Valid @RequestBody CreateAccountRequest accountRequest
    ){
        UserEntity user = accountService.createAccountStaff(INSTANCE.toModel(accountRequest));
        return CoreApiResponse.success("Insert staff successfully");
    }

    @GetMapping("/staffs")
    public CoreApiResponse<List<UserEntity>> getStaff() {
        List<UserEntity> users = accountService.getStaff();
        return CoreApiResponse.success(users);
    }

    @GetMapping("/managers")
    public CoreApiResponse<List<UserEntity>> getManager() {
        List<UserEntity> users = accountService.getManager();
        return CoreApiResponse.success(users);
    }

    @GetMapping("/shippers")
    public CoreApiResponse<List<UserEntity>> getShipper() {
        List<UserEntity> users = accountService.getShipper();
        return CoreApiResponse.success(users);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<UserEntity> getUserById(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.getUserById(id));
    }

    @PutMapping("/{id}")
    public CoreApiResponse<UserEntity> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return CoreApiResponse.success(accountService.updateUser(id, updateUserRequest));
    }
    @GetMapping
    public CoreApiResponse<List<UserEntity>> getAllUsers() {
        return CoreApiResponse.success(accountService.getAllUser());
    }

    @PutMapping("/{id}/ban")
    public CoreApiResponse<UserEntity> banUser(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.banUser(id));
    }

    @PutMapping("/{id}/unban")
    public CoreApiResponse<UserEntity> unbanUser(@PathVariable Long id) {
        return CoreApiResponse.success(accountService.unbanUser(id));
    }

    @GetMapping("/roles")
    public CoreApiResponse<List<RoleEntity>> getAllRoles() {
        return CoreApiResponse.success(accountService.getAllRoles());
    }
}