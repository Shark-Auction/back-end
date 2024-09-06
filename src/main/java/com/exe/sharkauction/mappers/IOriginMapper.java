package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.OriginEntity;
import com.exe.sharkauction.requests.OriginRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IOriginMapper {
    IOriginMapper INSTANCE = Mappers.getMapper(IOriginMapper.class);
    OriginEntity toModel(OriginRequest request);
}
