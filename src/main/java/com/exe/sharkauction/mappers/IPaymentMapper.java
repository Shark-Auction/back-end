package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.PaymentEntity;
import com.exe.sharkauction.requests.PaymentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface IPaymentMapper {
    IPaymentMapper INSTANCE = Mappers.getMapper(IPaymentMapper.class);
    @Mapping(source = "productId", target = "product.id")
    PaymentEntity toModel(PaymentRequest request);
}
