package com.exe.sharkauction.controllers;

import com.exe.sharkauction.components.apis.CoreApiResponse;
import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.requests.DeliverySenderRequest;
import com.exe.sharkauction.requests.DeliveryReceiverRequest;
import com.exe.sharkauction.services.IDeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public CoreApiResponse<DeliveryEntity> insertReceiverInformation(
            @Valid @RequestBody DeliveryReceiverRequest receiverRequest
    ) throws IOException {
        DeliveryEntity deliveryEntity = deliveryService.createReceiverDelivery(receiverRequest);
        return CoreApiResponse.success(deliveryEntity,"Thêm thông tin mới thành công");
    }

    @PutMapping("/{id}")
    public CoreApiResponse<DeliveryEntity> insertSenderInformation(
            @Valid @PathVariable Long id,
            @Valid @RequestBody DeliverySenderRequest senderRequest
    ) throws Exception {
        DeliveryEntity updatedEntity = deliveryService.updateSenderDelivery(id, senderRequest);
        return CoreApiResponse.success(updatedEntity,"Thêm thông tin mới thành công");
    }
}
