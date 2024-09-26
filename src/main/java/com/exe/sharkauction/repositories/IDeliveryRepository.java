package com.exe.sharkauction.repositories;

import com.exe.sharkauction.models.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDeliveryRepository extends JpaRepository<DeliveryEntity, Long> {
}
