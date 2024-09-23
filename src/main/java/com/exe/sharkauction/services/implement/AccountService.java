package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.constants.ImageContants;
import com.exe.sharkauction.components.exceptions.DataNotFoundException;
import com.exe.sharkauction.mappers.IUserMapper;
import com.exe.sharkauction.models.OriginEntity;
import com.exe.sharkauction.models.RoleEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.repositories.IRoleRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.requests.UpdateUserRequest;
import com.exe.sharkauction.services.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserMapper userMapper;




    @Override
    public UserEntity createAccountStaff(UserEntity user) {
        user.setEmail_verified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(ImageContants.DEFAULT_AVATAR);
        user.setIs_active(true);

        RoleEntity role = roleRepository.findById(3L).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole_id(role);

        return userRepository.save(user);
    }
    @Override
    public UserEntity createAccountShipper(UserEntity user) {
        user.setEmail_verified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(ImageContants.DEFAULT_AVATAR);
        user.setIs_active(true);

        RoleEntity role = roleRepository.findById(5L).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole_id(role);

        return userRepository.save(user);
    }

    @Override
    public UserEntity createAccountManager(UserEntity user) {
        user.setEmail_verified(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setImageUrl(ImageContants.DEFAULT_AVATAR);
        user.setIs_active(true);
        RoleEntity role = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole_id(role);

        return userRepository.save(user);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
    }

    @Override
    public UserEntity updateUser(Long id, UpdateUserRequest update) {
        UserEntity existingUser = userRepository
                .findById(id)
                .orElseThrow(() -> new DataNotFoundException("User", "id", id));

        userMapper.updateUserFromRequest(update, existingUser);

        if (update.getRoleId() != null && (existingUser.getRole_id() == null || !existingUser.getRole_id().getId().equals(update.getRoleId()))) {
            RoleEntity newRole = roleRepository
                    .findById(update.getRoleId())
                    .orElseThrow(() -> new DataNotFoundException("Role", "id", update.getRoleId()));

            existingUser.setRole_id(newRole);
        }

        if (update.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(update.getPassword()));
        }

        return userRepository.save(existingUser);
    }


    @Override
    public List<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity banUser(Long id) {
        UserEntity existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
        existingUser.setIs_active(false);
        return userRepository.save(existingUser);
    }


    @Override
    public UserEntity unbanUser(Long id) {
        UserEntity existingUser = userRepository
                .findById(id)
                .orElseThrow(()
                        -> new DataNotFoundException("User", "id", id));
        existingUser.setIs_active(true);
        return userRepository.save(existingUser);
    }

    @Override
    public List<UserEntity> getStaff() {
        Long fixedRoleId = 3L;
        return userRepository.findByRoleId_Id(fixedRoleId);
    }

    @Override
    public List<UserEntity> getShipper() {
        Long fixedRoleId = 5L;
        return userRepository.findByRoleId_Id(fixedRoleId);
    }

    @Override
    public List<UserEntity> getManager() {
        Long fixedRoleId = 2L;
        return userRepository.findByRoleId_Id(fixedRoleId);
    }

    @Override
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }
}
