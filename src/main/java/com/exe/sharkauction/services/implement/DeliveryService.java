package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.components.securities.UserPrincipal;
import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.enums.DeliveryStatus;
import com.exe.sharkauction.repositories.IDeliveryRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.requests.DeliverySenderRequest;
import com.exe.sharkauction.requests.DeliveryReceiverRequest;
import com.exe.sharkauction.services.IDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService implements IDeliveryService {
    private final IDeliveryRepository deliveryRepository;
//    private final IUserRepository userRepository;

    @Override
    public DeliveryEntity createReceiverDelivery(DeliveryReceiverRequest receiverRequest) {
//        UserEntity receiver = userRepository.findById(receiverRequest.getReceiverId())
//                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "There are no receiver with ID" + receiverRequest.getReceiverId()));
        DeliveryEntity deliveryEntity = new DeliveryEntity();

        deliveryEntity.setPaymentTypeId(receiverRequest.getPayment_type_id());
        deliveryEntity.setServiceTypeId(receiverRequest.getService_type_id());
        deliveryEntity.setToName(receiverRequest.getTo_name());
        deliveryEntity.setToPhone(receiverRequest.getTo_phone());
        deliveryEntity.setToAddress(receiverRequest.getTo_address());
        deliveryEntity.setToWardCode(receiverRequest.getTo_ward_code());
        deliveryEntity.setToDistrictId(receiverRequest.getTo_district_id());
        deliveryEntity.setProductId(receiverRequest.getProductID());
        deliveryEntity.setReceiver(this.getCurrentUser());
        deliveryEntity.setStatus(DeliveryStatus.RECEIVER_INFORMATION);

        // Save the new entity
        return deliveryRepository.save(deliveryEntity);

    }

    @Override
    public DeliveryEntity updateSenderDelivery(Long id, DeliverySenderRequest senderRequest) throws Exception {
        DeliveryEntity updatedDelivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This Delivery does not exist!"));

//        UserEntity sender = userRepository.findById(senderRequest.getSenderId())
//                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "There are no receiver with ID" + senderRequest.getSenderId()));

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
        updatedDelivery.setSender(this.getCurrentUser());
        updatedDelivery.setStatus(DeliveryStatus.SENDER_INFORMATION);

        try{
            updatedDelivery = deliveryRepository.save(updatedDelivery);
            updatedDelivery = GHNAPIService.createShippingOrder(updatedDelivery);
            return deliveryRepository.save(updatedDelivery);
        }
        catch (Exception ex){
            throw new Exception(ex);
        }

    }

    @Override
    public DeliveryEntity getDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "This delivery is not existed!"));
    }

    @Override
    public List<DeliveryEntity> getDeliveries() {
        return deliveryRepository.findAll();
    }

    @Override
    public List<DeliveryEntity> getMyDeliveriesSender() {
        return deliveryRepository.findBySender_Id(this.getCurrentUser().getId());
    }

    @Override
    public List<DeliveryEntity> getMyDeliveriesReceiver() {
        return deliveryRepository.findByReceiver_Id(this.getCurrentUser().getId());
    }

    @Override
    public List<DeliveryEntity> getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

//    @Override
//    public List<DeliveryEntity> getMyDeliveries() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//        UserEntity user = userPrincipal.getUser();
////        return deliveryRepository.(user);
//    }


    private UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }
}
