package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.models.enums.DeliveryStatus;
import com.exe.sharkauction.repositories.IDeliveryRepository;
import com.exe.sharkauction.services.implement.GHNAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliverySchedule {
    @Autowired
    private IDeliveryRepository deliveryRepository;

    @Scheduled(fixedRate = 7200000)
    public void checkDeliveryStatus() {
        List<DeliveryEntity> deliveries = deliveryRepository.findAll(); // Fetch all delivery entities or customize your query
        for (DeliveryEntity delivery : deliveries) {
            DeliveryStatus currentStatus = delivery.getStatus();
            if (currentStatus != null &&
                    currentStatus != DeliveryStatus.DAMAGE &&
                    currentStatus != DeliveryStatus.LOST &&
                    currentStatus != DeliveryStatus.CANCEL &&
                    currentStatus != DeliveryStatus.DELIVERED) {

                String orderCode = delivery.getOrderCode();
                DeliveryStatus updatedStatus = GHNAPIService.getOrderStatus(orderCode);

                if (updatedStatus != null) {
                    delivery.setStatus(updatedStatus);
                    deliveryRepository.save(delivery); // Save the updated delivery status
                    System.out.println("Updated status for order code " + orderCode + " to " + updatedStatus);
                }
            }
        }
    }
}
