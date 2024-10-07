package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.requests.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IOrderMapper {
    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

    @Mapping(target = "product.id", source = "product_id")
    @Mapping(target = "voucher.id", source = "voucher_id")

    OrderEntity toModel(OrderRequest request);


}
