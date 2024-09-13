package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.responses.UserProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserProfileResponse toResponse (UserEntity user);
}
