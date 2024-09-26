package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.models.enums.DeliveryStatus;
import com.exe.sharkauction.repositories.IDeliveryRepository;
import com.exe.sharkauction.requests.DeliverySenderRequest;
import com.exe.sharkauction.requests.DeliveryReceiverRequest;
import com.exe.sharkauction.services.IDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService implements IDeliveryService {
    private final IDeliveryRepository deliveryRepository;

    @Override
    public DeliveryEntity createReceiverDelivery(DeliveryReceiverRequest receiverRequest) {
        DeliveryEntity deliveryEntity = new DeliveryEntity();

        deliveryEntity.setPaymentTypeId(receiverRequest.getPayment_type_id());
        deliveryEntity.setServiceTypeId(receiverRequest.getService_type_id());
        deliveryEntity.setToName(receiverRequest.getTo_name());
        deliveryEntity.setToPhone(receiverRequest.getTo_phone());
        deliveryEntity.setToAddress(receiverRequest.getTo_address());
        deliveryEntity.setToWardCode(receiverRequest.getTo_ward_code());
        deliveryEntity.setToDistrictId(receiverRequest.getTo_district_id());
        deliveryEntity.setProductId(receiverRequest.getProductID());
        deliveryEntity.setStatus(DeliveryStatus.RECEIVER_INFORMATION);

        // Save the new entity
        return deliveryRepository.save(deliveryEntity);

    }

    @Override
    public DeliveryEntity updateSenderDelivery(Long id, DeliverySenderRequest senderRequest) throws Exception {
        DeliveryEntity updatedDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This Delivery does not exist!"));
        // Set product and dimensions details
        updatedDelivery.setWeight(senderRequest.getWeight());
        updatedDelivery.setLength(senderRequest.getLength());
        updatedDelivery.setWidth(senderRequest.getWidth());
        updatedDelivery.setHeight(senderRequest.getHeight());
        updatedDelivery.setNote(senderRequest.getNote());

        // Set sender information from the request
        updatedDelivery.setFromName(senderRequest.getFrom_name());
        updatedDelivery.setFromPhone(senderRequest.getFrom_phone());
        updatedDelivery.setFromAddress(senderRequest.getFrom_address());
        updatedDelivery.setFromWardName(senderRequest.getFrom_ward_name());
        updatedDelivery.setFromDistrictName(senderRequest.getFrom_district_name());
        updatedDelivery.setFromProvinceName(senderRequest.getFrom_province_name());
        updatedDelivery.setProductId(senderRequest.getProductID());
        updatedDelivery.setStatus(DeliveryStatus.SENDER_INFORMATION);

        try{
            updatedDelivery = deliveryRepository.save(updatedDelivery);
            GHNAPIService.createShippingOrder(updatedDelivery);
            updatedDelivery.setStatus(DeliveryStatus.WAITING_RECEIVING);
            return updatedDelivery;
        }
        catch (Exception ex){
            throw new Exception(ex);
        }

    }
}
