package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    List<DeliveryEntity> findBySender_Id(Long senderId);
    List<DeliveryEntity> findByReceiver_Id(Long receiverId);

    @Query("SELECT d FROM DeliveryEntity d WHERE d.productId = (SELECT o.product.id FROM OrderEntity o WHERE o.id = :orderId)")
    List<DeliveryEntity> findByOrderId(@Param("orderId") Long orderId);
}
