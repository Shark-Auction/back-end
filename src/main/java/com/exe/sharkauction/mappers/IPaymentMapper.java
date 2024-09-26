package com.exe.sharkauction.mappers;

import com.exe.sharkauction.models.PaymentEntity;
import com.exe.sharkauction.requests.PaymentRequest;
import org.mapstruct.factory.Mappers;

public interface IPaymentMapper {
    IPaymentMapper INSTANCE = Mappers.getMapper(IPaymentMapper.class);

    PaymentEntity toModel(PaymentRequest request);
}
