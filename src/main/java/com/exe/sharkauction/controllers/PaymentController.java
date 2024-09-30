package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.mappers.IPaymentMapper;
import com.exe.sharkauction.models.PaymentEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.requests.PaymentRequest;
import com.exe.sharkauction.requests.PaymentResponseRequest;
import com.exe.sharkauction.requests.ProductRequest;
import com.exe.sharkauction.responses.PaymentResponse;
import com.exe.sharkauction.services.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.exe.sharkauction.mappers.IProductMapper.INSTANCE;

@RestController
@RequestMapping("${app.api.version.v1}/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("")
    public CoreApiResponse<PaymentResponse.PaymentData> createProduct(
            @Valid @RequestBody PaymentRequest request
    ) throws Exception {
        PaymentResponse.PaymentData paymentData = paymentService.createPaymentLink(IPaymentMapper.INSTANCE.toModel(request));
        return CoreApiResponse.success(paymentData,"Thêm sản phẩm mới thành công");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<PaymentEntity> updateProduct(
            @Valid @RequestBody PaymentResponseRequest paymentResponseRequest
    ){
        PaymentEntity payment = paymentService.returnValueOfPayment(paymentResponseRequest);
        return CoreApiResponse.success(payment, "");
    }
}
