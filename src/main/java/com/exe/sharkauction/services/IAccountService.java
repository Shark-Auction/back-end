package com.exe.sharkauction.services;

import com.exe.sharkauction.models.RoleEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.requests.UpdateUserRequest;

import java.util.List;

public interface IAccountService {
    UserEntity createAccountStaff(UserEntity user);

    UserEntity createAccountShipper(UserEntity user);

    UserEntity createAccountManager(UserEntity user);

    UserEntity getUserById(Long id);
    UserEntity updateUser(Long id, UpdateUserRequest update);

    List<UserEntity> getAllUser();

    UserEntity banUser(Long id);

    List<UserEntity> getStaff();

    List<UserEntity> getShipper();

    List<RoleEntity> getAllRoles();

    List<UserEntity> getManager();
}
