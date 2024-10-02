package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.ViolateEntity;
import com.exe.sharkauction.models.VoucherEntity;
import com.exe.sharkauction.requests.ViolateRequest;
import com.exe.sharkauction.requests.VoucherRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IVoucherMapper {
    IVoucherMapper INSTANCE = Mappers.getMapper(IVoucherMapper.class);

    @Mapping(source = "userId", target = "user.id")
    VoucherEntity toModel(VoucherRequest request);

}
