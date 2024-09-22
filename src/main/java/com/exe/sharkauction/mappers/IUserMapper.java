package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.requests.CreateAccountRequest;
import com.exe.sharkauction.requests.UpdateUserRequest;
import com.exe.sharkauction.responses.UserProfileResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfileResponse toResponse (UserEntity user);

    UserEntity toModel(CreateAccountRequest request);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updatePersonalFromRequest(PersonalUpdateRequest updateRequest, @MappingTarget UserEntity user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateUserRequest updateRequest, @MappingTarget UserEntity user);
}
