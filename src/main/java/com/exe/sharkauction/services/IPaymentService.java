package com.exe.sharkauction.services;

import com.exe.sharkauction.models.PaymentEntity;
import com.exe.sharkauction.requests.PaymentRequest;
import com.exe.sharkauction.requests.PaymentResponseRequest;
import com.exe.sharkauction.responses.PaymentResponse;

public interface IPaymentService {
    PaymentResponse.PaymentData createPaymentLink(PaymentEntity payment) throws Exception;

    PaymentEntity returnValueOfPayment(PaymentResponseRequest paymentResponseRequest);
}
