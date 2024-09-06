package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.BrandEntity;
import com.exe.sharkauction.requests.BrandRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper
public interface IBrandMapper {
    IBrandMapper INSTANCE = Mappers.getMapper(IBrandMapper.class);
    BrandEntity toModel(BrandRequest request);
}
