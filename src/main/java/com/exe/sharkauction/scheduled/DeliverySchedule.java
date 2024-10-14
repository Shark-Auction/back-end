package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.models.DeliveryEntity;
import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.models.enums.DeliveryStatus;
import com.exe.sharkauction.models.enums.OrderStatus;
import com.exe.sharkauction.repositories.IDeliveryRepository;
import com.exe.sharkauction.repositories.IOrderRepository;
import com.exe.sharkauction.services.implement.GHNAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliverySchedule {
    @Autowired
    private IDeliveryRepository deliveryRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Scheduled(fixedRate = 600000)
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
                OrderEntity order = orderRepository.findByProductId(delivery.getProductId());
                if(updatedStatus == DeliveryStatus.DELIVERED){
                    order.setStatus(OrderStatus.delivered);
                }
                else if(updatedStatus == DeliveryStatus.WAITING_RECEIVING){
                    order.setStatus(OrderStatus.processing);
                }
                else if(updatedStatus == DeliveryStatus.PICKED){
                    order.setStatus(OrderStatus.shipping);
                }
                else if(updatedStatus == DeliveryStatus.CANCEL){
                    order.setStatus(OrderStatus.cancelled);
                }
                orderRepository.save(order);


                if (updatedStatus != null) {
                    delivery.setStatus(updatedStatus);
                    deliveryRepository.save(delivery); // Save the updated delivery status
                    System.out.println("Updated status for order code " + orderCode + " to " + updatedStatus);
                }
            }
        }
    }
}
