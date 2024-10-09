package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.CashOutEntity;
import com.exe.sharkauction.requests.CashOutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ICashOutMapper {

    ICashOutMapper INSTANCE = Mappers.getMapper(ICashOutMapper.class);
    CashOutEntity toModel(CashOutRequest request);
}
