package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.requests.ViolateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IViolateMapper {
    IViolateMapper INSTANCE = Mappers.getMapper(IViolateMapper.class);

    @Mapping(source = "userId", target = "user.id")
    ViolateEntity toModel(ViolateRequest request);


}
