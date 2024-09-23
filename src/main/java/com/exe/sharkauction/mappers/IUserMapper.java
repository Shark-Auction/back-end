package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.requests.CreateAccountRequest;
import com.exe.sharkauction.requests.UpdateUserRequest;
import com.exe.sharkauction.responses.UserProfileResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfileResponse toResponse (UserEntity user);

    UserEntity toModel(CreateAccountRequest request);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, @MappingTarget UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "roleId", target = "role_id.id")
    void updateUserFromRequest(UpdateUserRequest updateRequest, @MappingTarget UserEntity user);
}
