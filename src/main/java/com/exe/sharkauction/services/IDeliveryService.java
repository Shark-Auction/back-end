package com.exe.sharkauction.services;

import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.requests.DeliverySenderRequest;
import com.exe.sharkauction.requests.DeliveryReceiverRequest;

import java.util.List;

public interface IDeliveryService {
    public DeliveryEntity createReceiverDelivery(DeliveryReceiverRequest receiverRequest);
    public DeliveryEntity updateSenderDelivery(Long id, DeliverySenderRequest senderRequest) throws Exception;
    public DeliveryEntity getDelivery(Long id);
    public List<DeliveryEntity> getDeliveries();
    public List<DeliveryEntity> getMyDeliveriesSender();
    public List<DeliveryEntity> getMyDeliveriesReceiver();
}
