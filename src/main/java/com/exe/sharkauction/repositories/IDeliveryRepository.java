package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
    List<DeliveryEntity> findBySender_Id(Long senderId);
    List<DeliveryEntity> findByReceiver_Id(Long receiverId);

}
