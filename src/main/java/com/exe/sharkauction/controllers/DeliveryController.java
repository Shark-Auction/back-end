package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.requests.DeliverySenderRequest;
import com.exe.sharkauction.requests.DeliveryReceiverRequest;
import com.exe.sharkauction.services.IDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${app.api.version.v1}/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final IDeliveryService deliveryService;

    @GetMapping("")
    public CoreApiResponse<List<DeliveryEntity>> getDeliveries(){
        return CoreApiResponse.success(deliveryService.getDeliveries());
    }

    @GetMapping("/{id}")
    public CoreApiResponse<DeliveryEntity> getDelivery(
            @Valid @PathVariable Long id
    ){
        return CoreApiResponse.success(deliveryService.getDelivery(id));
    }

    @PostMapping("/receiver")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<DeliveryEntity> insertReceiverInformation(
            @Valid @RequestBody DeliveryReceiverRequest receiverRequest
    ) throws IOException {
        DeliveryEntity deliveryEntity = deliveryService.createReceiverDelivery(receiverRequest);
        return CoreApiResponse.success(deliveryEntity,"Thêm thông tin mới thành công");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<DeliveryEntity> insertSenderInformation(
            @Valid @PathVariable Long id,
            @Valid @RequestBody DeliverySenderRequest senderRequest
    ) throws Exception {
        DeliveryEntity updatedEntity = deliveryService.updateSenderDelivery(id, senderRequest);
        return CoreApiResponse.success(updatedEntity,"Thêm thông tin mới thành công");
    }

    @GetMapping("/receiver/me")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<List<DeliveryEntity>> getMyDeliveriesReceiver(){
        List<DeliveryEntity> deliveryEntities = deliveryService.getMyDeliveriesReceiver();
        return CoreApiResponse.success(deliveryEntities, "Lấy thông tin thành công");
    }

    @GetMapping("/sender/me")
    @PreAuthorize("hasRole('USER')")
    public CoreApiResponse<List<DeliveryEntity>> getMyDeliveriesSender(){
        List<DeliveryEntity> deliveryEntities = deliveryService.getMyDeliveriesSender();
        return CoreApiResponse.success(deliveryEntities, "Lấy thông tin thành công");
    }

    @GetMapping("/order/{id}")
    public CoreApiResponse<List<DeliveryEntity>> getDeliveryByOrderID(
            @Valid @PathVariable Long id
    ){
        List<DeliveryEntity> deliveryEntities = deliveryService.getDeliveryByOrderId(id);
        return CoreApiResponse.success(deliveryEntities, "Lấy thông tin thành công");
    }
}
